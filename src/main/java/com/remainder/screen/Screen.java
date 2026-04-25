package com.remainder.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.remainder.util.Stage;


public abstract class Screen implements com.badlogic.gdx.Screen {
    protected Screen lastScreen;
    public Stage stage;
    public Batch batch;
    /**
     * Pause is currently unused.
     * Override {@link #update(float)} and {@link #render(float, float)} to implement custom pause behavior.
     */
    public boolean pause = false;

    @Override
    public void show() {
//        stage.addListener(new InputListener() {
//            @Override
//            public boolean keyUp(InputEvent event, int keycode) {
//                if (keycode == Input.Keys.ESCAPE) Gdx.app.postRunnable(() -> ScreenManager.instance.toLastScreen());
//                return false;
//            }
//        });
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        render(delta, 1f);
    }

    public void render(float delta, float alpha) {
        stage.draw(alpha);

        batch.begin();
        renderOther(delta, alpha);
        batch.end();
    }

    public void renderOther(float delta, float alpha) {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
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

    public String getTag() {
        return getClass().getSimpleName();
    }

    public TextureRegion getFBO(FrameBuffer fbo, float delta) {
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
