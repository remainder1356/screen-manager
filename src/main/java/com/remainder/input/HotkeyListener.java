package com.remainder.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.remainder.util.AutoLogger;

import java.util.HashMap;
import java.util.Map;

import static com.remainder.input.ComboKey.*;

public class HotkeyListener extends InputListener implements AutoLogger {
    private final IntMap<Array<Runnable>> hotkeys = new IntMap<>();
    private final Map<ComboKey, Array<Runnable>> comboKeys = new HashMap<>();

    /**
     * Single key hotkey
     * 
     * @param keycode For example: Input.Keys.ESCAPE, Input.Keys.ENTER
     */
    public Runnable registerHotkey(int keycode, Runnable callback) {
        if (!hotkeys.containsKey(keycode)) {
            hotkeys.put(keycode, new Array<>(true, 1));
        }
        hotkeys.get(keycode).add(callback);
        return callback;
    }

    /**
     * Single key hotkey with CTRL modifier
     *
     * @param keycode For example: Input.Keys.ESCAPE -> CTRL + ESCAPE, Input.Keys.ENTER -> CTRL + ENTER
     */
    public Runnable registerHotkeyWithCtrl(int keycode, Runnable callback) {
        Runnable finalCallback = () -> {
            if (isCtrlPressed()) {
                callback.run();
            }
        };
        registerHotkey(keycode, finalCallback);
        return finalCallback;
    }

    /**
     * Single key hotkey with ALT modifier
     *
     * @param keycode For example: Input.Keys.ESCAPE -> ALT + ESCAPE, Input.Keys.ENTER -> ALT + ENTER
     */
    public Runnable registerHotkeyWithAlt(int keycode, Runnable callback) {
        Runnable finalCallback = () -> {
            if (isAltPressed()) {
                callback.run();
            }
        };
        registerHotkey(keycode, finalCallback);
        return finalCallback;
    }

    /**
     * Single key hotkey with SHIFT modifier
     *
     * @param keycode For example: Input.Keys.ESCAPE -> SHIFT + ESCAPE, Input.Keys.ENTER -> SHIFT + ENTER
     */
    public Runnable registerHotkeyWithShift(int keycode, Runnable callback) {
        Runnable finalCallback = () -> {
            if (isShiftPressed()) {
                callback.run();
            }
        };
        registerHotkey(keycode, finalCallback);
        return finalCallback;
    }


    public void unregisterHotkey(int keycode) {
        hotkeys.remove(keycode);
    }

    public void unregisterHotkey(int keycode, Runnable callback) {
        if (!hotkeys.containsKey(keycode)) {
            error("Hotkey not found");
            return;
        }

        hotkeys.get(keycode).removeValue(callback, true);
    }
    
    /**
     * Combination key hotkey.
     * Do not use combination keys like "ALT+F4" or "CTRL+SPACE".
     * If you want to use combination keys like that,
     * use {@code Gdx.input.isKeyPressed(keycode)} in callback and register single hotkey "CTRL" or else.
     * @param keyCombination For example: "CTRL+A"
     */
    public Runnable registerComboKey(ComboKey keyCombination, Runnable callback) {
        if (!comboKeys.containsKey(keyCombination)) {
            comboKeys.put(keyCombination, new Array<>(true, 1));
        }

        comboKeys.get(keyCombination).add(callback);
        return callback;
    }

    public void unregisterComboKey(ComboKey keyCombination) {
        comboKeys.remove(keyCombination);
    }

    public void unregisterComboKey(ComboKey keyCombination, Runnable callback) {
        if (!comboKeys.containsKey(keyCombination)) {
            error("ComboKey not found");
            return;
        }

        comboKeys.get(keyCombination).removeValue(callback, true);
    }

    public void clear() {
        hotkeys.clear();
        comboKeys.clear();
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        // 检查是否有注册的单个按键热键
        return hotkeys.containsKey(keycode);
    }
    
    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        boolean result = false;
        // 检查组合键（在按键释放时检查，以确保修饰键状态准确）
        ComboKey comboKey = buildComboKey(keycode);
        
        if (comboKeys.containsKey(comboKey)) {
            Gdx.app.postRunnable(() -> comboKeys.get(comboKey).forEach(Runnable::run));
            result = true;
        }
        
        // 检查是否有注册的单个按键热键
        if (hotkeys.containsKey(keycode)) {
            Gdx.app.postRunnable(() -> hotkeys.get(keycode).forEach(Runnable::run));
            result = true;
        }
        
        return result;
    }
    
    /**
     * 构建组合键字符串，用于匹配注册的组合键
     * 
     * @param keycode 当前按下的键
     * @return 组合键字符串
     */
    private ComboKey buildComboKey(int keycode) {
        return new ComboKey(keycode, isCtrlPressed(), isAltPressed(), isShiftPressed());
    }

    private boolean findComboKey(int keycode) {
        return comboKeys.keySet().stream().anyMatch(key -> key.keycode() == keycode);
    }

    public IntMap<Array<Runnable>> getHotkeys() {
        return hotkeys;
    }

    public Map<ComboKey, Array<Runnable>> getComboKeys() {
        return comboKeys;
    }
}
