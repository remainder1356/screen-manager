package com.remainder.screen.transition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class FadeScreenTransition extends ScreenTransition {
    public static float defaultDuration = 0.5f;

    public FadeScreenTransition() {
        this(defaultDuration);
    }

    public FadeScreenTransition(float duration) {
        super(duration);
    }

    public FadeScreenTransition(float duration, Interpolation interpolation) {
        super(duration, interpolation);
    }

    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        super.render(last, cur, delta, batch);

        // Blends the two screens
        Color c = batch.getColor();

        batch.setColor(c.r, c.g, c.b, progress);
        batch.draw(last, 0, 0, last.getRegionWidth(), last.getRegionHeight());

        batch.setColor(c.r, c.g, c.b, 1-progress);
        batch.draw(cur, 0, 0, cur.getRegionWidth(), cur.getRegionHeight());
        batch.setColor(c.r, c.g, c.b, 1);
    }
}
