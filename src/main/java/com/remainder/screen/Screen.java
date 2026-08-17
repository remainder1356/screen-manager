package com.remainder.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.remainder.input.HotkeyListener;
import com.remainder.input.HudStage;
import com.remainder.util.AutoLogger;
import com.remainder.input.Stage;


public abstract class Screen implements com.badlogic.gdx.Screen, AutoLogger {
    protected Screen lastScreen;
    public Stage stage;
    public HudStage hudStage;
    public HotkeyListener hotkeyListener;
    public Batch batch;
    /**
     * Pause is currently unused.
     * Override {@link #update(float)} and {@link #render(float)} to implement custom pause behavior.
     */
    public boolean pause = false;

    @Override
    public void show() {

    }

    public void update(float delta) {
        stage.act(delta);
        hudStage.act(delta);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
        hudStage.resize(width, height);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (hudStage != null) hudStage.dispose();
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public void hide() {

    }

    protected TextureRegion getFBO(FrameBuffer fbo, float delta) {
        fbo.begin();
        ScreenUtils.clear(ScreenManager.instance.clearColor, true);
        render(delta);
        fbo.end();

        Texture texture = fbo.getColorBufferTexture();

        // flip the texture
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);

        return textureRegion;
    }
}
