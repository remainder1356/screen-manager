package util;

import java.util.function.Function;

public class UpdatableString implements LabelList.Updatable {
    public String string;
    private final Function<Float, String> func;

    public UpdatableString(String string, Function<Float, String> func) {
        this.string = string;
        this.func = func;
    }

    public UpdatableString(Function<Float, String> func) {
        this.string = func.apply(0f);
        this.func = func;
    }

    @Override
    public void update(float delta) {
        string = func.apply(delta);
    }

    @Override
    public String toString() {
        return string;
    }
}
