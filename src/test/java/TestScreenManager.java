import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.remainder.screen.Screen;
import com.remainder.screen.ScreenManager;
import com.remainder.screen.transition.FadeScreenTransition;
import com.remainder.util.AutoLogger;

import java.util.Arrays;

public class TestScreenManager {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Test");
        config.setWindowedMode(1200, 600);

        new Lwjgl3Application(new Main1(), config);
    }
}

class Main1 extends ScreenManager {
    @Override
    public void create() {
        super.create();
        VisUI.load();

        setScreen(new Screen1());
//        setScreen(new Screen2());
//        setScreen(new Screen3());
//        toLastScreen();
//        toLastScreen();

        log(Arrays.toString(lasts.toArray()));
    }
}

class Screen1 extends Screen implements AutoLogger {
    @Override
    public void show() {
        super.show();
        log("Screen1 show");

        Pixmap pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.FOREST);
        pixmap.fill();
        Image image1 = new Image(new Texture(pixmap));
        Image image2 = new Image(new Texture(pixmap));
        image2.setColor(Color.RED);
        pixmap.dispose();
        hudStage.addActor(image1);
        stage.addActor(image2);

        image1.setPosition(50, 50);
        image2.setPosition(50, 50);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        OrthographicCamera camera = (OrthographicCamera) ScreenManager.instance.getViewport().getCamera();

        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            camera.zoom += 0.02f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom -= 0.02f;
            camera.zoom = Math.max(camera.zoom, 0.5f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-100 * 0.02f, 0, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(100 * 0.02f, 0, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 100 * 0.02f, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -100 * 0.02f, 0);
        }

        camera.update();
    }
}

class Screen2 extends Screen implements AutoLogger {
    Image image;
    @Override
    public void show() {
        super.show();
        log("Screen2 show");

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.FOREST);
        pixmap.fill();
        image = new Image(new Texture(pixmap));
        pixmap.dispose();
        stage.addUIActor(image);

        image.setPosition(
                100 + ScreenManager.instance.getScreenWidth() / 2 - image.getWidth() / 2,
                ScreenManager.instance.getScreenHeight() / 2 - image.getHeight() / 2);

        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Gdx.app.postRunnable(() -> {
                    ScreenManager.instance.setScreen(new Screen1(), new FadeScreenTransition());
                });
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        image.setPosition(
                100 + ScreenManager.instance.getScreenWidth() / 2 - image.getWidth() / 2,
                ScreenManager.instance.getScreenHeight() / 2 - image.getHeight() / 2);

    }
}

class Screen3 extends Screen implements AutoLogger {
    @Override
    public void show() {
        super.show();
        log("Screen3 show");
    }
}
