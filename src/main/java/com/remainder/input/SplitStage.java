package com.remainder.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplitStage extends Stage{
    public SplitStage() {
        super();
        init();
    }

    public SplitStage(Viewport viewport) {
        super(viewport);
        init();
    }

    public SplitStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        init();
    }

    private Group tables;
    private Table top, bottom, left, right, center, topLeft, topRight, bottomLeft, bottomRight;
    private float split = 0.4f;
    private float offTop = 0, offBottom = 0, offLeft = 0, offRight = 0;

    private void init() {
        if (tables == null) tables = new Group();
        if (top == null) top = new Table();
        if (bottom == null) bottom = new Table();
        if (left == null) left = new Table();
        if (right == null) right = new Table();
        if (center == null) center = new Table();
        if (topLeft == null) topLeft = new Table();
        if (topRight == null) topRight = new Table();
        if (bottomLeft == null) bottomLeft = new Table();
        if (bottomRight == null) bottomRight = new Table();

        refreshTables();

        tables.addActor(top);
        tables.addActor(bottom);
        tables.addActor(left);
        tables.addActor(right);
        tables.addActor(center);
        tables.addActor(topLeft);
        tables.addActor(topRight);
        tables.addActor(bottomLeft);
        tables.addActor(bottomRight);

        addUIActor(tables);
    }

    public void refreshTables() {
        if (tables == null) {
            init();
            return;
        }

        float sWidth = getWidth() * split;
        float sHeight = getHeight() * split;
        float usWidth = getWidth() * (1 - split);
        float usHeight = getHeight() * (1 - split);

        float x0 = offLeft;
        float x1 = offLeft + sWidth;
        float x2 = usWidth - offRight;
        float x3 = getWidth() - offRight;
        float y0 = offTop;
        float y1 = offTop + sHeight;
        float y2 = usHeight - offBottom;
        float y3 = getHeight() - offBottom;

        bottomLeft.setBounds(x0, y0, x1-x0, y1-y0);
        bottom.setBounds(x1, y0, x2-x1, y1-y0);
        bottomRight.setBounds(x2, y0, x3-x2, y1-y0);

        left.setBounds(x0, y1, x1-x0, y2-y1);
        center.setBounds(x1, y1, x2-x1, y2-y1);
        right.setBounds(x2, y1, x3-x2, y2-y1);

        topLeft.setBounds(x0, y2, x1-x0, y3-y2);
        top.setBounds(x1, y2, x2-x1, y3-y2);
        topRight.setBounds(x2, y2, x3-x2, y3-y2);
    }

    public void refreshOffsets() {
        Viewport v = getViewport();
        setOffLeft(-v.getLeftGutterWidth());
        setOffTop(-v.getBottomGutterHeight());
        setOffRight(-v.getRightGutterWidth());
        setOffBottom(-v.getTopGutterHeight());

        refreshTables();
    }

    public void setSkin(Skin skin) {
        top.setSkin(skin);
        bottom.setSkin(skin);
        left.setSkin(skin);
        right.setSkin(skin);
        center.setSkin(skin);
        topLeft.setSkin(skin);
        topRight.setSkin(skin);
        bottomLeft.setSkin(skin);
        bottomRight.setSkin(skin);
    }

    public Table getTop() {
        return top;
    }

    public void setTop(Table top) {
        this.top = top;
    }

    public Table getBottom() {
        return bottom;
    }

    public void setBottom(Table bottom) {
        this.bottom = bottom;
    }

    public Table getLeft() {
        return left;
    }

    public void setLeft(Table left) {
        this.left = left;
    }

    public Table getRight() {
        return right;
    }

    public void setRight(Table right) {
        this.right = right;
    }

    public Table getCenter() {
        return center;
    }

    public void setCenter(Table center) {
        this.center = center;
    }

    public float getSplit() {
        return split;
    }

    public void setSplit(float split) {
        this.split = split;
    }

    public float getOffTop() {
        return offTop;
    }

    public void setOffTop(float offTop) {
        this.offTop = offTop;
    }

    public float getOffBottom() {
        return offBottom;
    }

    public void setOffBottom(float offBottom) {
        this.offBottom = offBottom;
    }

    public float getOffLeft() {
        return offLeft;
    }

    public void setOffLeft(float offLeft) {
        this.offLeft = offLeft;
    }

    public float getOffRight() {
        return offRight;
    }

    public void setOffRight(float offRight) {
        this.offRight = offRight;
    }

    public Table getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Table topLeft) {
        this.topLeft = topLeft;
    }

    public Table getTopRight() {
        return topRight;
    }

    public void setTopRight(Table topRight) {
        this.topRight = topRight;
    }

    public Table getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Table bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public Table getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Table bottomRight) {
        this.bottomRight = bottomRight;
    }

    public Group getSpecificTables() {
        return tables;
    }

    public Table getSpecificTables(int align) {
        return switch (align) {
            case Align.top -> top;
            case Align.bottom -> bottom;
            case Align.left -> left;
            case Align.right -> right;
            case Align.center -> center;
            case Align.topLeft -> topLeft;
            case Align.topRight -> topRight;
            case Align.bottomLeft -> bottomLeft;
            case Align.bottomRight -> bottomRight;
            default -> null;
        };
    }
}
