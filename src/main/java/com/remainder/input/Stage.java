package com.remainder.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.remainder.util.PriorityGroup;
import com.remainder.util.ReflectUtil;

public class Stage extends com.badlogic.gdx.scenes.scene2d.Stage{
    public Stage() {
        super();
        setRoot(new PriorityGroup());
    }

    public Stage(Viewport viewport) {
        super(viewport);
        setRoot(new PriorityGroup());
    }

    public Stage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        setRoot(new PriorityGroup());
    }

    @Override
    public void draw() {
        draw(1);
    }

    public PriorityGroup getPriorityGroup() {
        return (PriorityGroup) getRoot();
    }

    /**
     * draw with alpha
     * @param alpha 0 ~ 1
     */
    public void draw(float alpha) {
        Camera camera = getViewport().getCamera();
        camera.update();

        if (!getRoot().isVisible()) return;

        Batch batch = getBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        getRoot().draw(batch, alpha);
        batch.end();

        // debug
        // This is not a good way, but now I don't want to refactor Stage
        if(ReflectUtil.getBool(com.badlogic.gdx.scenes.scene2d.Stage.class, this, "debug")) {
            ReflectUtil.getAndInvoke(com.badlogic.gdx.scenes.scene2d.Stage.class, this, "drawDebug");
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void setViewport(Viewport viewport) {
        super.setViewport(viewport);
    }

    @Override
    public void addActor(Actor actor) {
        addActor(actor, 0);
    }

    public void addActor(Actor actor, int priority) {
        ((PriorityGroup)getRoot()).addActor(actor, priority);
    }

    public void addUIActor(Actor actor) {
        addActor(actor, 0x40000000);
    }
}
