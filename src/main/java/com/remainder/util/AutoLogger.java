package com.remainder.util;

import com.badlogic.gdx.Gdx;

public interface AutoLogger {
    default void log(String message) {
        Gdx.app.log(getTag(), message);
    }

    default void error(String message) {
        Gdx.app.error(getTag(), message);
    }

    default void error(String message, Throwable throwable) {
        Gdx.app.error(getTag(), message, throwable);
    }

    default void debug(String message) {
        Gdx.app.debug(getTag(), message);
    }

    default String getTag() {
        return getClass().getSimpleName();
    }
}
