package com.remainder.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.remainder.util.AutoLogger;
import com.remainder.util.Stage;


public abstract class Screen implements com.badlogic.gdx.Screen, AutoLogger {
    protected Screen lastScreen;
    public Stage stage;
    public Batch batch;
    /**
     * Pause is currently unused.
     * Override {@link #update(float)} and {@link #render(float)} to implement custom pause behavior.
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
        stage.draw();

        batch.begin();
        renderOther(delta);
        batch.end();
    }

    public void renderOther(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        if (lastScreen != null) lastScreen.dispose();
        if (stage != null) stage.dispose();
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
