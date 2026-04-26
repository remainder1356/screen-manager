package com.remainder.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.remainder.screen.transition.FadeScreenTransition;
import com.remainder.screen.transition.ScreenTransition;
import com.remainder.util.AutoLogger;
import com.remainder.util.Stage;

public abstract class ScreenManager implements ApplicationListener, AutoLogger {
    public static ScreenManager instance;

    public Color clearColor = Color.TAN;

    protected Viewport viewport;

    protected Screen lastScreen;
    protected Screen curScreen;
    protected Screen nextScreen;
    protected ScreenTransition screenTransition;
    protected boolean isTransitioning;
    protected float transitionTime;
    protected float transitionDuration;
    protected boolean autoDispose = false;
    protected int currentWidth, currentHeight;
    protected FrameBuffer fbo;
    protected boolean hasDepth;

    @Override
    public void create() {
        currentWidth = Gdx.graphics.getWidth();
        currentHeight = Gdx.graphics.getHeight();
        viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            new OrthographicCamera());

        instance = this;

        if (fbo != null) {
            fbo.dispose();
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,
                HdpiUtils.toBackBufferX(currentWidth),
                HdpiUtils.toBackBufferY(currentHeight), hasDepth);
    }

    public void setScreen(Screen screen) {
        setScreen(screen, null, false);
    }

    public void setScreen(Screen screen, ScreenTransition transition) {
        setScreen(screen, transition, false);
    }

    public void setScreen(Screen screen, ScreenTransition transition, boolean debug) {
        log("Setting: " + screen.getTag());

        if (isTransitioning) {
            finishTransition();
        }

        screen.stage = new Stage(viewport);
        screen.stage.setDebugAll(debug);
        screen.batch = screen.stage.getBatch();
        Gdx.input.setInputProcessor(screen.stage);
        screen.show();

        // last(s1) - cur(s2) - next(-)     s1.last -> null     s2.last -> s1
        // last(s1) - cur(s2) - next(s1)    s1.last -> s2       s2.last -> null
        nextScreen = screen;
        if (curScreen != null && curScreen.lastScreen == nextScreen) {
            curScreen.lastScreen = null;
        }
        screen.lastScreen = curScreen;

        if (transition != null) {
            screenTransition = transition;
            isTransitioning = true;
            transitionTime = 0;
            transitionDuration = transition.getDuration();
            log("Processing Transition: " + transition);
        } else {
            nextScreen();
        }
    }

    public void toLastScreen() {
        if (hasLastScreen()) {
            setScreen(lastScreen, new FadeScreenTransition());
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
        screenTransition.setProgress(transitionTime);

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
        log("Finish transition.");
    }

    private void nextScreen() {
        if (curScreen != null) {
            curScreen.hide();

            if (autoDispose) {
                curScreen.dispose();
            }
        }

        curScreen = nextScreen;
        lastScreen = nextScreen.lastScreen;

        nextScreen = null;
    }

    public void render(float delta) {
        ScreenUtils.clear(clearColor);

        if (isTransitioning) {
            if (screenTransition != null) {
                if (curScreen == null || nextScreen == null) {
                    finishTransition();
                    return;
                }
                screenTransition.render(curScreen.getFBO(fbo, delta), nextScreen.getFBO(fbo, delta), delta, nextScreen.batch);
            }
        } else {
            if (curScreen != null) {
                curScreen.render(delta);
            }
        }

//        if (curScreen != null) {
//            curScreen.batch.begin();
//            displayList.draw(curScreen.batch, 1f);
//            curScreen.batch.end();
//        }
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
}
