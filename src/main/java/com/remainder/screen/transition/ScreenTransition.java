package com.remainder.screen.transition;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public abstract class ScreenTransition {
    protected float duration;
    protected float time;
    protected float progress;
    protected Interpolation interpolation;

    public ScreenTransition(float duration) {
        this(duration, Interpolation.linear);
    }

    public ScreenTransition(float duration, Interpolation interpolation) {
        this.duration = duration;
        this.interpolation = interpolation;
    }

    public float getDuration() {
        return duration;
    }

    public float getProgress() {
        return progress;
    }

    public void setTime(float time) {
        this.time = time;
        progress = interpolation.apply(time/duration);
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        time += delta;
    }

    @Override
    public String toString() {
        return String.format("%s [ %.2f / %.2f s]",getClass().getSimpleName(), time, duration);
    }
}

