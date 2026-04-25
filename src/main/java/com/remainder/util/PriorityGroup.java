package com.remainder.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Lower priorities are processed first.
 * Some methods are deprecated as they are no longer required.
 * Note that {@link #addActorAt} is a special case: its int parameter is now used as the priority.
 */
public class PriorityGroup extends Group {
    public PriorityGroup() {
        super();
    }

    public HashMap<Actor, Integer> priorities = new HashMap<>();

    public void addActor(Actor actor, int priority) {
        super.addActor(actor);
        priorities.put(actor, priority);

        getChildren().sort(Comparator.comparingInt(a -> priorities.get(a)));
    }

    @Override
    @Deprecated
    public boolean swapActor(int first, int second) {
        return false;
    }

    @Override
    @Deprecated
    public boolean swapActor(Actor first, Actor second) {
        return false;
    }

    @Override
    @Deprecated
    public void addActorAfter(Actor actorAfter, Actor actor) {
        addActor(actor);
    }

    @Override
    @Deprecated
    public void addActorBefore(Actor actorBefore, Actor actor) {
        addActor(actor);
    }

    @Override
    @Deprecated
    public void addActorAt(int priority, Actor actor) {
        addActor(actor, priority);
    }

    @Override
    public void addActor(Actor actor) {
        addActor(actor, 0);
    }
}
