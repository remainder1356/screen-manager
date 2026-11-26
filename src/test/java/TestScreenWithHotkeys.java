import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.remainder.input.ComboKey;
import com.remainder.input.SplitStage;
import com.remainder.screen.Screen;
import com.remainder.screen.ScreenManager;
import com.remainder.util.ReflectUtil;
import com.remainder.util.font.DefaultFont;
import util.LabelList;

/**
 * 测试带有热键功能的屏幕
 */
public class TestScreenWithHotkeys extends Screen {
    @Override
    public void show() {
        super.show();

        VisUI.load();

        stage = new SplitStage(stage.getViewport(), stage.getBatch());

        SplitStage sStage = (SplitStage) stage;
        sStage.setDebugAll(true);
        sStage.setSkin(VisUI.getSkin());

        Table left = sStage.getLeft();
        left.add("Hello, here is left!");

        Table right = sStage.getRight();
        right.add("Hello, here is right!");

        Table top = sStage.getTop();
        top.add("Hello, here is top!");

        Table bottom = sStage.getBottom();
        bottom.add("Hello, here is bottom!");

        Table center = sStage.getCenter();
        center.add("Hello, here is center!");

        Table topLeft = sStage.getTopLeft();
        topLeft.add("Hello, here is top left!");

        Table topRight = sStage.getTopRight();
        topRight.add("Hello, here is top right!");

        Table bottomLeft = sStage.getBottomLeft();
        bottomLeft.add("Hello, here is bottom left!");

        Table bottomRight = sStage.getBottomRight();
        bottomRight.add("Hello, here is bottom right!");

        // 注册ESC键返回上一屏
        hotkeyListener.registerHotkey(Input.Keys.ESCAPE, () -> {
            if (ScreenManager.instance.hasLastScreen()) {
                ScreenManager.instance.toLastScreen();
            } else {
                Gdx.app.exit();
            }
        });

        // 注册空格键
        hotkeyListener.registerHotkey(Input.Keys.SPACE, () -> {
            System.out.println("空格键被按下");
        });

        // 注册Ctrl+S
        hotkeyListener.registerComboKey(ComboKey.ctrl(Input.Keys.S), () -> {
            System.out.println("Ctrl+S被按下");
        });

        // 注册Ctrl+空格键
        hotkeyListener.registerHotkeyWithCtrl(Input.Keys.SPACE, () -> {
            System.out.println("Ctrl+空格键被按下");
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        SplitStage sStage = (SplitStage) stage;
        sStage.refreshOffsets();
    }

    @Override
    public String getTag() {
        return "TestScreenWithHotkeys";
    }

    public static class TestHotkey extends ScreenManager{
        @Override
        public void create() {
            super.create();

            setScreen(new TestScreenWithHotkeys());
        }

        public static void main(String[] args) {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setTitle("TestHotkey");
            config.setWindowedMode(800, 600);
            new Lwjgl3Application(new TestHotkey(), config);
        }
    }
}