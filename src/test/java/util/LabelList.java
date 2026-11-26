package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;

/**
 * 标签列表组件，用于垂直显示多个可更新的文本项。
 * 继承自Widget并实现Styleable接口，支持样式自定义。
 */
public class LabelList extends Widget{
    private final Array<Updatable> items;

    private Label.LabelStyle style;
    private float prefWidth, prefHeight;
    float itemHeight, bgAlpha = 1f;
    private int alignment = Align.left;

    /**
     * 构造一个LabelList实例。
     *
     * @param items 要显示的更新项数组
     * @param style 标签样式，定义字体、颜色等外观属性
     * @param alignment 文本对齐方式，使用Align类的常量
     */
    public LabelList(Array<Updatable> items, Label.LabelStyle style, int alignment) {
        this.items = items;
        this.style = style;
        this.alignment = alignment;
    }

    /**
     * 构造一个LabelList实例，默认左对齐。
     *
     * @param items 要显示的更新项数组
     * @param style 标签样式，定义字体、颜色等外观属性
     */
    public LabelList(Array<Updatable> items, Label.LabelStyle style) {
        this(items, style, Align.left);
    }

    /**
     * 构造一个空的LabelList实例，默认左对齐。
     *
     * @param style 标签样式，定义字体、颜色等外观属性
     */
    public LabelList(Label.LabelStyle style) {
        this(new Array<>(), style, Align.left);
    }

    /**
     * 更新所有子项的状态。
     * 遍历所有项并调用其update方法。
     *
     * @param delta 时间增量，表示距离上一帧的时间间隔
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        // 更新所有子项
        for (int i = 0; i < items.size; i++) {
            Updatable item = items.get(i);
            item.update(delta);
        }
    }

    /**
     * 计算布局信息，包括每项高度和首选尺寸。
     * 根据字体特性和背景drawable计算每个项目的高度和整体首选宽高。
     */
    @Override
    public void layout() {
        BitmapFont font = style.font;

        // 计算项目高度
        itemHeight = font.getCapHeight() - font.getDescent() * 2;

        // 计算首选宽度，找出最长的文本项
        prefWidth = 0;
        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        for (int i = 0; i < items.size; i++) {
            layout.setText(font, items.get(i).toString());
            prefWidth = Math.max(layout.width, prefWidth);
        }
        layoutPool.free(layout);

        // 计算首选高度
        prefHeight = items.size * itemHeight;

        // 考虑背景drawable的尺寸
        Drawable background = style.background;
        if (background != null) {
            prefWidth = Math.max(prefWidth + background.getLeftWidth() + background.getRightWidth(), background.getMinWidth());
            prefHeight = Math.max(prefHeight + background.getTopHeight() + background.getBottomHeight(), background.getMinHeight());
        }
    }

    /**
     * 绘制标签列表。
     * 依次绘制每个非空项目的背景和文本内容，支持垂直滚动裁剪。
     *
     * @param batch 批处理器，用于绘制图形
     * @param parentAlpha 父组件的透明度值
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        BitmapFont font = style.font;
        Color fontColor = style.fontColor;
        Color origin = batch.getColor().cpy();

        float x = getX(), y = getY(), width = getWidth(), height = getHeight();
        float itemY = height;

        // 根据背景调整绘制区域
        Drawable background = style.background;
        if (background != null) {
            float leftWidth = background.getLeftWidth();
            x += leftWidth;
            itemY -= background.getTopHeight();
            width -= leftWidth + background.getRightWidth();
        }

        drawBackground(batch, parentAlpha);

        // 设置字体颜色并绘制所有项目
        font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
        Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = layoutPool.obtain();
        for (int i = 0; i < items.size; i++) {
            Updatable item = items.get(i);

            if (item != null && !item.toString().isEmpty()) {
                layout.setText(font, items.get(i).toString());
                // move down to make the text vertically centered
                drawItem(batch, font, i, item, x, y + itemY - (itemHeight - layout.height)/2, width);
            }

            itemY -= itemHeight;

            // 超出可视区域则停止绘制
            if (itemY < 0) {
                break;
            }
        }
        layoutPool.free(layout);
        font.setColor(origin);
    }

    /**
     * 绘制指定索引项的背景。
     * 默认实现绘制样式中定义的背景drawable。
     *
     * @param parentAlpha 父组件的透明度值
     */
    protected void drawBackground (Batch batch, float parentAlpha) {
        Drawable background = style.background;
        if (background == null) {
            return;
        }

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha * bgAlpha);
        background.draw(batch,
                getX() + background.getLeftWidth(),
                getY() + background.getBottomHeight(),
                getPrefWidth() - background.getLeftWidth() - background.getRightWidth(),
                getPrefHeight() - background.getBottomHeight() - background.getTopHeight());
    }

    /**
     * 绘制单个标签项的文本内容。
     *
     * @param batch 批处理器
     * @param font 使用的位图字体
     * @param index 当前项的索引
     * @param item 要绘制的更新项
     * @param x 绘制的x坐标
     * @param y 绘制的y坐标
     * @param width 可用宽度
     * @return 文本布局对象，包含文本尺寸信息
     */
    protected GlyphLayout drawItem (Batch batch, BitmapFont font, int index, Updatable item, float x, float y, float width) {
        String string = item.toString();
        return font.draw(batch, string, x, y, 0, string.length(), width, alignment, false, "...");
    }

    /**
     * 根据y坐标获取对应的项。
     *
     * @param y y坐标值（相对于组件）
     * @return 对应位置的项，如果不在任何项上则返回null
     */
    public @Null Updatable getItemAt (float y) {
        int index = getItemIndexAt(y);
        if (index == -1) return null;
        return items.get(index);
    }

    /**
     * 根据y坐标获取对应项的索引。
     *
     * @param y y坐标值（相对于组件）
     * @return 对应位置的项索引，如果不在任何项上则返回-1
     */
    public int getItemIndexAt (float y) {
        float height = getHeight();
        Drawable background = style.background;
        // 根据背景调整高度和y坐标
        if (background != null) {
            height -= background.getTopHeight() + background.getBottomHeight();
            y -= background.getBottomHeight();
        }
        // 计算索引位置
        int index = (int)((height - y) / itemHeight);
        if (index < 0 || index >= items.size) return -1;
        return index;
    }

    /**
     * 添加一个项到列表末尾。
     *
     * @param item 要添加的更新项，不能为null
     * @throws IllegalArgumentException 如果item为null
     */
    public void addItem (Updatable item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.add(item);

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
    }

    public void addItem (String item) {
        addItem(new UpdatableString(item, i -> item));
    }

    /**
     * 添加多个项到列表。
     *
     * @param newItems 要添加的更新项数组，不能为null
     * @throws IllegalArgumentException 如果newItems为null
     */
    public void addItems (Updatable... newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.clear();
        items.addAll(newItems);

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
    }

    /**
     * 添加多个项到列表。
     *
     * @param newItems 要添加的更新项数组，不能为null
     * @throws IllegalArgumentException 如果newItems为null
     */
    public void addItems (Array<Updatable> newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        // 避免与当前数组相同时清空
        if (newItems != items) {
            items.clear();
            items.addAll(newItems);
        }

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
    }

    /**
     * 清空列表中的所有项。
     */
    public void clearItems () {
        if (items.size == 0) return;
        items.clear();
        invalidateHierarchy();
    }

    /**
     * 获取所有项的数组。
     *
     * @return 更新项数组
     */
    public Array<Updatable> getItems () {
        return items;
    }

    /**
     * 获取单个项的高度。
     *
     * @return 项高度值
     */
    public float getItemHeight () {
        return itemHeight;
    }

    /**
     * 设置文本对齐方式。
     *
     * @param alignment 对齐方式，使用Align类的常量
     */
    public void setAlignment (int alignment) {
        this.alignment = alignment;
    }

    /**
     * 获取文本对齐方式。
     *
     * @return 对齐方式值
     */
    public int getAlignment () {
        return alignment;
    }

    /**
     * 获取背景透明度。
     *
     * @return 背景透明度值，范围0-1
     */
    public float getBgAlpha() {
        return bgAlpha;
    }

    /**
     * 设置背景透明度。
     *
     * @param bgAlpha 背景透明度值，范围0-1
     */
    public void setBgAlpha(float bgAlpha) {
        this.bgAlpha = bgAlpha;
    }

    /**
     * 获取首选宽度。
     *
     * @return 首选宽度值
     */
    @Override
    public float getPrefWidth () {
        validate();
        return prefWidth;
    }

    /**
     * 获取首选高度。
     *
     * @return 首选高度值
     */
    @Override
    public float getPrefHeight () {
        validate();
        return prefHeight;
    }

    /**
     * 获取当前样式。
     *
     * @return 标签样式对象
     */
    public Label.LabelStyle getStyle() {
        return style;
    }

    /**
     * 设置样式。
     * 更改样式后会触发层级失效以重新计算布局。
     *
     * @param style 新的标签样式，不能为null
     * @throws IllegalArgumentException 如果style为null
     */
    public void setStyle(Label.LabelStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    public interface Updatable {
        void update(float delta);
    }
}
