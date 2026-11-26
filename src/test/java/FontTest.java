import com.badlogic.gdx.AbstractInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.VisUI;
import com.remainder.screen.Screen;
import com.remainder.screen.ScreenManager;
import com.remainder.util.ReflectUtil;
import util.LabelList;
import util.Styles;
import util.UpdatableString;

import java.lang.reflect.Field;

public class FontTest {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 800);
        config.setTitle("FontTest");
        config.setTransparentFramebuffer(true);
        config.setDecorated(false);
        new Lwjgl3Application(new FontTestGame(), config);
    }
}

class FontTestGame extends ScreenManager {
    Field pressedKeys;

    public FontTestGame() {
        clearColor = Color.CLEAR;
    }

    @Override
    public void create() {
        super.create();
        VisUI.load();
        setFloating(true);

        setScreen(new FontTestScreen());
    }
}

class FontTestScreen extends Screen {
    LabelList labels;
    Field pressedKeys;

    @Override
    public void show() {
        super.show();

        pressedKeys = ReflectUtil.getField(AbstractInput.class, "pressedKeys");

        labels = new LabelList(Styles.labelStyle);
        labels.addItem(new UpdatableString(delta -> "FPS: " + Gdx.graphics.getFramesPerSecond()));
        labels.addItem(new UpdatableString(delta -> "DELTA TIME: " + Gdx.graphics.getDeltaTime()));
        labels.addItem(new UpdatableString(delta -> {
            try {
                boolean[] keys = (boolean[]) pressedKeys.get(Gdx.input);
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i]) {
                        if (!first) {
                            builder.append(" | ");
                        }

                        builder.append(Input.Keys.toString(i));
                        first = false;
                    }
                }

                if (builder.isEmpty()) builder.append("NONE");

                return "PRESSED KEYS: " + builder;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));

        labels.setBounds(0, 0,
                (float) ScreenManager.instance.getCurrentWidth() /2,
                ScreenManager.instance.getCurrentHeight());
        labels.setBgAlpha(0.4f);
        stage.addUIActor(labels);
        stage.setDebugAll(true);
    }
}
