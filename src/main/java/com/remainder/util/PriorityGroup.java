package com.remainder.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Lower priorities will be processed first. Higher priorities will be drawn at higher layers.
 * Method swapActor(int first, int second) is deprecated as it is no longer required.
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

    /**
     * Swap two actors and their priorities.
     * @return {@link Group#swapActor(int, int)}
     */
    @Override
    public boolean swapActor(Actor first, Actor second) {
        boolean r = super.swapActor(first, second);

        if (r) {
            priorities.put(first, priorities.get(second));
            priorities.put(second, priorities.get(first));
        }

        return r;
    }

    /**
     * Add actor to the group with a priority one more than actorAfter.
     */
    @Override
    public void addActorAfter(Actor actorAfter, Actor actor) {
        addActor(actor, priorities.getOrDefault(actorAfter, 0)+1);
    }

    /**
     * Add actor to the group with a priority one less than actorBefore.
     */
    @Override
    public void addActorBefore(Actor actorBefore, Actor actor) {
        addActor(actor, priorities.getOrDefault(actorBefore, 0)-1);
    }

    /**
     * Add an actor to the group.
     */
    @Override
    public void addActorAt(int priority, Actor actor) {
        addActor(actor, priority);
    }

    @Override
    public void addActor(Actor actor) {
        addActor(actor, 0);
    }
}
