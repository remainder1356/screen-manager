package com.remainder.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.Objects;

public record ComboKey(int keycode, boolean ctrl, boolean alt, boolean shift) {
    public static ComboKey shift(int keycode) {
        return new ComboKey(keycode, false, false, true);
    }

    public static ComboKey ctrl(int keycode) {
        return new ComboKey(keycode, true, false, false);
    }

    public static ComboKey alt(int keycode) {
        return new ComboKey(keycode, false, true, false);
    }

    public static ComboKey ctrlAlt(int keycode) {
        return new ComboKey(keycode, true, true, false);
    }

    public static ComboKey shiftAlt(int keycode) {
        return new ComboKey(keycode, false, true, true);
    }

    public static ComboKey shiftCtrl(int keycode) {
        return new ComboKey(keycode, true, false, true);
    }

    public static ComboKey all(int keycode) {
        return new ComboKey(keycode, true, true, true);
    }

    public static boolean isCtrlPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    public static boolean isAltPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
    }

    public static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ComboKey(int keycode1, boolean ctrl1, boolean alt1, boolean shift1))) return false;
        return keycode == keycode1 && alt == alt1 && ctrl == ctrl1 && shift == shift1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keycode, ctrl, alt, shift);
    }
}
