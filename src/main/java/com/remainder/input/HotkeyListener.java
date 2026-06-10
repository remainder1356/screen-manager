package com.remainder.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.IntMap;

import java.util.HashMap;
import java.util.Map;

import static com.remainder.input.ComboKey.*;

public class HotkeyListener extends InputListener {
    private final IntMap<Runnable> hotkeys = new IntMap<>();
    private final Map<ComboKey, Runnable> comboKeys = new HashMap<>();
    
    /**
     * Single key hotkey
     * 
     * @param keycode For example: Input.Keys.ESCAPE, Input.Keys.ENTER
     */
    public void registerHotkey(int keycode, Runnable callback) {
        hotkeys.put(keycode, callback);
    }

    public void unregisterHotkey(int keycode) {
        hotkeys.remove(keycode);
    }
    
    /**
     * Combination key hotkey
     * 
     * @param keyCombination For example: "CTRL+C", "ALT+F4"
     */
    public void registerComboKey(ComboKey keyCombination, Runnable callback) {
        comboKeys.put(keyCombination, callback);
    }

    public void unregisterComboKey(ComboKey keyCombination) {
        comboKeys.remove(keyCombination);
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
        // 检查组合键（在按键释放时检查，以确保修饰键状态准确）
        ComboKey comboKey = buildComboKey(keycode);
        
        if (comboKeys.containsKey(comboKey)) {
            Gdx.app.postRunnable(comboKeys.get(comboKey));
            return true;
        }
        
        // 检查是否有注册的单个按键热键
        if (hotkeys.containsKey(keycode)) {
            Gdx.app.postRunnable(hotkeys.get(keycode));
            return true;
        }
        
        return false;
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
}
