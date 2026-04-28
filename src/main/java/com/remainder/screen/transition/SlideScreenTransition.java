package com.remainder.screen.transition;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

/**
 * Supporting the screen slide in from left, right, top and bottom.
 */
public class SlideScreenTransition extends ScreenTransition {
    public static final int FROM_LEFT = 0;
    public static final int FROM_RIGHT = 1;
    public static final int FROM_TOP = 2;
    public static final int FROM_BOTTOM = 3;

    public static float defaultDuration = 0.5f;

    public SlideScreenTransition(int slideDirection) {
        this(defaultDuration, slideDirection);
    }

    public SlideScreenTransition(Interpolation interpolation, int slideDirection) {
        this(defaultDuration, interpolation, slideDirection);
    }

    public SlideScreenTransition(float duration, int slideDirection) {
        super(duration);
        this.slideDirection = slideDirection;
    }

    public SlideScreenTransition(float duration, Interpolation interpolation, int slideDirection) {
        super(duration, interpolation);
        this.slideDirection = slideDirection;
    }

    private final int slideDirection;

    @Override
    public void render(TextureRegion last, TextureRegion cur, float delta, Batch batch) {
        super.render(last, cur, delta, batch);

        float p = getProgress();
        int width = cur.getRegionWidth();
        int height = cur.getRegionHeight();

        switch (slideDirection) {
            case FROM_LEFT:
                // last screen slide out from right side, current screen slide in from left side
                batch.draw(last, width * p, 0, width, height);
                batch.draw(cur, -width + width * p, 0, width, height);
                break;

            case FROM_RIGHT:
                // last screen slide out from left side, current screen slide in from right side
                batch.draw(last, -width * p, 0, width, height);
                batch.draw(cur, width - width * p, 0, width, height);
                break;

            case FROM_TOP:
                // last screen slide out from bottom side, current screen slide in from top side
                batch.draw(last, 0, -height * p, width, height);
                batch.draw(cur, 0, height - height * p, width, height);
                break;

            case FROM_BOTTOM:
                // last screen slide out from top side, current screen slide in from bottom side
                batch.draw(last, 0, height * p, width, height);
                batch.draw(cur, 0, -height + height * p, width, height);
                break;

            default:
                // default with FROM_LEFT
                batch.draw(last, width * p, 0, width, height);
                batch.draw(cur, -width + width * p, 0, width, height);
                break;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - " + slideDirection;
    }
}
