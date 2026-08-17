package com.remainder.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A HUD (Heads-Up Display) stage that maintains actor positions relative to
 * their nearest screen edges when the viewport is resized.
 * <p>
 * When {@link #resize(int, int)} is called, each actor's size remains unchanged,
 * and its position is adjusted so that the distance to its nearest edge
 * (left/right for x-axis, bottom/top for y-axis) stays the same.
 * <p>
 * This is useful for UI elements that should stay anchored to a specific corner
 * or edge of the screen, such as score displays, minimaps, or buttons.
 */
public class HudStage extends com.badlogic.gdx.scenes.scene2d.Stage {

    public HudStage() {
        super();
    }

    public HudStage(Viewport viewport) {
        super(viewport);
    }

    public HudStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    /**
     * Resizes the stage and repositions all actors so that each actor maintains
     * its original size and its distance to the nearest edge.
     * <p>
     * For each actor on both x and y axes:
     * <ul>
     *   <li>The distances to both edges are compared;</li>
     *   <li>The actor is anchored to the nearer edge, keeping that distance unchanged;</li>
     *   <li>If the actor is exactly centered, it remains centered.</li>
     * </ul>
     *
     * @param width  the new screen width in pixels
     * @param height the new screen height in pixels
     */
    public void resize(int width, int height) {
        Viewport vp = getViewport();
        float oldWorldWidth = vp.getWorldWidth();
        float oldWorldHeight = vp.getWorldHeight();

        // Skip repositioning if dimensions haven't changed
        if (oldWorldWidth == width && oldWorldHeight == height) {
            vp.update(width, height, true);
            return;
        }

        // Collect all actors from the root group
        Array<Actor> actors = getRoot().getChildren();
        int n = actors.size;

        // Pre-calculate each actor's distance to all four edges in old world coordinates
        float[] leftDists = new float[n];
        float[] rightDists = new float[n];
        float[] bottomDists = new float[n];
        float[] topDists = new float[n];

        for (int i = 0; i < n; i++) {
            Actor actor = actors.get(i);
            float ax = actor.getX();
            float ay = actor.getY();
            float aw = actor.getWidth();
            float ah = actor.getHeight();

            leftDists[i] = ax;
            rightDists[i] = oldWorldWidth - (ax + aw);
            bottomDists[i] = ay;
            topDists[i] = oldWorldHeight - (ay + ah);
        }

        // Update the viewport with the new screen dimensions
        vp.update(width, height, true);

        // Get new world dimensions after viewport update
        float newWorldWidth = vp.getWorldWidth();
        float newWorldHeight = vp.getWorldHeight();

        // Reposition each actor based on its nearest edge
        for (int i = 0; i < n; i++) {
            Actor actor = actors.get(i);

            // X-axis: anchor to the nearer horizontal edge
            float newX;
            if (leftDists[i] <= rightDists[i]) {
                // Anchor to left edge
                newX = leftDists[i];
            } else {
                // Anchor to right edge
                newX = newWorldWidth - actor.getWidth() - rightDists[i];
            }

            // Y-axis: anchor to the nearer vertical edge
            float newY;
            if (bottomDists[i] <= topDists[i]) {
                // Anchor to bottom edge
                newY = bottomDists[i];
            } else {
                // Anchor to top edge
                newY = newWorldHeight - actor.getHeight() - topDists[i];
            }

            actor.setPosition(newX, newY);
        }
    }
}