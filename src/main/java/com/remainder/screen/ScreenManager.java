package com.remainder.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.remainder.input.HotkeyListener;
import com.remainder.screen.transition.FadeScreenTransition;
import com.remainder.screen.transition.ScreenTransition;
import com.remainder.util.AutoLogger;
import com.remainder.util.Stage;
import com.remainder.util.font.DefaultFont;

import java.util.Objects;
import java.util.Stack;

import org.lwjgl.glfw.GLFW;

/**
 * Manages the lifecycle and transitions between different screens in the application.
 * 
 * <p>This class maintains the current screen, a stack of previous screens, and handles
 * screen transitions using FrameBuffers to capture screen states during transitions.</p>
 */
public abstract class ScreenManager implements ApplicationListener, AutoLogger {
    /**
     * Global instance of the ScreenManager, allowing access from anywhere in the application.
     */
    public static ScreenManager instance;

    /**
     * The color used to clear the screen before rendering.
     * Default is CLEAR, but can be customized.
     */
    public Color clearColor = Color.CLEAR;

    /**
     * The camera used for rendering the screen content.
     */
    protected Camera camera;
    
    /**
     * The viewport that defines how the camera maps to screen coordinates.
     */
    protected Viewport viewport;

    /**
     * Stack containing the history of screens that were previously active.
     * Used to implement the "go back to previous screen" functionality.
     */
    protected Stack<Screen> lasts;
    
    /**
     * The screen that was previously active before the current screen.
     * Derived from the top of the lasts stack.
     * If autoDispose is true, the last screen will always be null.
     */
    protected Screen lastScreen;
    
    /**
     * The currently active screen.
     */
    protected Screen curScreen;
    
    /**
     * The screen that will become active after the current transition completes.
     * Used during transition animations.
     */
    protected Screen nextScreen;
    
    /**
     * The transition effect currently being applied between screens.
     */
    protected ScreenTransition screenTransition;
    
    /**
     * Flag indicating whether a screen transition is currently in progress.
     */
    protected boolean isTransitioning;
    
    /**
     * The elapsed time since the current transition began.
     * Used to control animation timing during transitions.
     */
    protected float transitionTime;
    
    /**
     * The total duration of the current transition.
     * When transitionTime reaches this value, the transition completes.
     */
    protected float transitionDuration;
    
    /**
     * Flag determining whether screens should be automatically disposed
     * when they are no longer needed.
     */
    protected boolean autoDispose;
    
    /**
     * Current width of the application window/surface.
     */
    protected int currentWidth, currentHeight;
    
    /**
     * Frame buffers used to capture screen content during transitions.
     * lastFBO stores the content of the current screen during transition.
     * curFBO stores the content of the next screen during transition.
     */
    protected FrameBuffer lastFBO, curFBO;
    
    /**
     * Flag indicating whether the frame buffer should include a depth buffer.
     */
    protected boolean hasDepth;

    /**
     * Internal flag used to indicate that we're transitioning to a previous screen.
     * This affects how the screen history stack is managed.
     */
    private boolean toLast = false;

    /**
     * The window handle, used to set glfw attributes.
     */
    private long window;

    /**
     * Initializes the ScreenManager and sets up the basic rendering infrastructure.
     *
     * <p>This method is called once during application startup. It initializes:</p>
     * <ul>
     *   <li>Viewport and camera for rendering</li>
     *   <li>Frame buffers for screen transition effects</li>
     *   <li>Screen history stack</li>
     *   <li>Global instance reference</li>
     * </ul>
     *
     * <p>If frame buffers already exist, they are disposed before creating new ones.</p>
     */
    @Override
    public void create() {
        currentWidth = Gdx.graphics.getWidth();
        currentHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(currentWidth, currentHeight, camera);

        if (lastFBO != null) {
            lastFBO.dispose();
        }
        lastFBO = new FrameBuffer(Pixmap.Format.RGBA8888,
                HdpiUtils.toBackBufferX(currentWidth),
                HdpiUtils.toBackBufferY(currentHeight), hasDepth);
        if (curFBO != null) {
            curFBO.dispose();
        }
        curFBO = new FrameBuffer(Pixmap.Format.RGBA8888,
                HdpiUtils.toBackBufferX(currentWidth),
                HdpiUtils.toBackBufferY(currentHeight), hasDepth);

        lasts = new Stack<>();

        instance = this;
    }

    /**
     * Sets the current screen to the specified screen without transition.
     * @param screen The screen to set as the current screen.
     */
    public void setScreen(Screen screen) {
        setScreen(screen, null, false);
    }

    public void setScreen(Screen screen, ScreenTransition transition) {
        setScreen(screen, transition, false);
    }

    public void setScreen(Screen screen, ScreenTransition transition, boolean debug) {
        debug("Start setting screen: " + screen.getTag());

        if (isTransitioning) {
            finishTransition();
        }

        // initialize stage
        screen.stage = new Stage(viewport);
        screen.hotkeyListener = new HotkeyListener();
        screen.stage.setDebugAll(debug);
        screen.stage.addListener(screen.hotkeyListener);
        screen.batch = screen.stage.getBatch();
        Gdx.input.setInputProcessor(screen.stage);
        screen.show();

        nextScreen = screen;

        // push history screen
        if (!toLast && curScreen != null) {
            lasts.push(curScreen);
        }
        toLast = false;
        lastScreen = lasts.isEmpty() ? null : lasts.peek();

        // transition process
        if (transition != null) {
            screenTransition = transition;
            isTransitioning = true;
            transitionTime = 0;
            transitionDuration = transition.getDuration();
            debug("Start processing transition.");
        } else {
            nextScreen();
        }
    }

    public void toLastScreen() {
        toLastScreen(new FadeScreenTransition(), false);
    }

    /**
     * Here is the logic of the screen transition
     * A -> B -> A -> C => B(cur) == toLast()
     * A -> B -> A -> C <= B == toLast()
     * A -> B -> A => C(cur)
     */
    public void toLastScreen(ScreenTransition transition, boolean debug) {
        if (hasLastScreen()) {
            toLast = true;
            setScreen(Objects.requireNonNull(lasts.pop()), transition, debug);
        }else {
            error("The screen is not exists.");
        }
    }

    public Screen getCurScreen() {
        return curScreen;
    }

    public Screen getLastScreen() {
        if (hasLastScreen()) return lastScreen;
        error("The screen is not exists.");
        return null;
    }

    public void update(float delta) {
        if (isTransitioning) {
            updateTransition(delta);
        }else if (curScreen != null) {
            curScreen.update(delta);
        }
    }

    private void updateTransition(float delta) {
        transitionTime += delta;
        screenTransition.setTime(transitionTime);

        if (transitionTime >= transitionDuration) {
            finishTransition();
        }
    }

    private void finishTransition() {
        nextScreen();
        transitionTime = 0;
        transitionDuration = 0;
        screenTransition = null;
        isTransitioning = false;
        debug("Finish transition.");
    }

    private void nextScreen() {
        if (curScreen != null) {
            curScreen.hide();

            if (autoDispose) {
                curScreen.dispose();
            }
        }

        curScreen = nextScreen;
        curScreen.lastScreen = lastScreen;

        nextScreen = null;
    }

    public void render(float delta) {
        ScreenUtils.clear(clearColor, true);

        if (isTransitioning) {
            if (screenTransition != null) {
                if (curScreen == null || nextScreen == null) {
                    finishTransition();
                    return;
                }
                screenTransition.render(curScreen.getFBO(lastFBO, delta), nextScreen.getFBO(curFBO, delta),
                        delta, nextScreen.batch);
            } else {
                error("Lost screen transition in a unrealizable way.");
            }
        } else {
            if (curScreen != null) {
                curScreen.render(delta);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (curScreen != null) {
            curScreen.resize(width, height);
        }
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {
        if (curScreen != null) {
            curScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (curScreen != null) {
            curScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (curScreen != null) {
            curScreen.hide();
            curScreen.dispose();
            curScreen = null;
        }

        lasts.forEach(screen -> {
            if (screen != null) {
                screen.dispose();
            }
        });

        // dispose default font
        if (DefaultFont.getFont() != null) {
            DefaultFont.getFont().dispose();
        }
    }

    public boolean isTransitioning() {
        return isTransitioning;
    }

    public boolean isAutoDispose() {
        return autoDispose;
    }

    public void setAutoDispose(boolean autoDispose) {
        this.autoDispose = autoDispose;
    }

    public boolean hasLastScreen() {
        return lastScreen != null;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(int currentWidth) {
        this.currentWidth = currentWidth;
        resize(currentWidth, currentHeight);
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(int currentHeight) {
        this.currentHeight = currentHeight;
        resize(currentWidth, currentHeight);
    }

    public ScreenTransition getScreenTransition() {
        return screenTransition;
    }

    public void setFloating(boolean value) {
        setWindow(GLFW.GLFW_FLOATING, value);
    }

    public void setMousePassThrough(boolean value) {
        setWindow(GLFW.GLFW_MOUSE_PASSTHROUGH, value);
    }

    protected void setWindow(int attrib, boolean value) {
        GLFW.glfwSetWindowAttrib(getWindowHandle(), attrib, (value) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    protected long getWindowHandle() {
        if (window == 0) {
            log("Try to get window handle.");
            if (Gdx.graphics instanceof Lwjgl3Graphics graphics) {
                window = graphics.getWindow().getWindowHandle();
                log("Get window handle success!");
            }else {
                error("Get window handle failed!");
            }
        }

        return window;
    }
}
