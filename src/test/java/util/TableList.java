package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;

/**
 * 标签列表组件，用于垂直显示多个可更新的文本项。
 * 继承自Widget并实现Styleable接口，支持样式自定义。
 */
public class TableList extends WidgetGroup{
    private final Array<Table> items;

    private TableListStyle style;
    private float prefWidth, prefHeight;
    float itemHeight, bgAlpha = 1f;

    /**
     * 构造一个LabelList实例。
     *
     * @param items 要显示的Table数组
     * @param style 定义背景等外观属性
     */
    public TableList(Array<Table> items, TableListStyle style) {
        this.items = items;
        this.style = style;
    }

    /**
     * 构造一个空的LabelList实例
     *
     * @param style 定义背景等外观属性
     */
    public TableList(TableListStyle style) {
        this(new Array<>(), style);
    }

    /**
     * 构造一个LabelList实例，并且不使用样式。
     */
    public TableList() {
        this(new Array<>(), new TableListStyle());
    }

    /**
     * 计算布局信息，包括每项高度和首选尺寸。
     * 根据字体特性和背景drawable计算每个项目的高度和整体首选宽高。
     */
    @Override
    public void layout() {
        // 计算首选宽度、高度
        prefWidth = 0;
        prefHeight = 0;
        for (int i = 0; i < items.size; i++) {
            Table item = items.get(i);
            prefWidth = Math.max(item.getPrefWidth(), prefWidth);
            prefHeight += item.getPrefHeight();
        }

        // 考虑背景drawable的尺寸
        Drawable background = style.background;
        if (background != null) {
            prefWidth = Math.max(prefWidth + background.getLeftWidth() + background.getRightWidth(), background.getMinWidth());
            prefHeight = Math.max(prefHeight + background.getTopHeight() + background.getBottomHeight(), background.getMinHeight());
        }

        float x = 0;
        float itemY = getHeight();
        if (background != null) {
            itemY -= background.getTopHeight();
            x += background.getLeftWidth();
        }

        // 设置Table的位置，大小，背景
        for (int i = 0; i < items.size; i++) {
            Table item = items.get(i);

            itemY -= item.getPrefHeight();
            item.setBounds(x, itemY, item.getPrefWidth(), prefHeight);
            item.setBackground(background);
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
        if (style.background != null){
            validate();
            drawBackground(batch, parentAlpha);
        }

        // 绘制所有项目
        super.draw(batch, parentAlpha);
    }

    /**
     * 绘制指定索引项的背景。
     * 默认实现绘制样式中定义的背景drawable。
     *
     * @param batch 批处理器
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
     * 根据y坐标获取对应的项。
     *
     * @param y y坐标值（相对于组件）
     * @return 对应位置的项，如果不在任何项上则返回null
     */
    public @Null Table getItemAt (float y) {
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
    public void addItem (Table item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.add(item);
        addActor(item);

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
    }

    /**
     * 添加多个项到列表。
     *
     * @param newItems 要添加的更新项数组，不能为null
     * @throws IllegalArgumentException 如果newItems为null
     */
    public void addItems (Table... newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.addAll(newItems);
        for (Table item : newItems) {
            addActor(item);
        }

        invalidate();
        if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
    }

    /**
     * 添加多个项到列表。
     *
     * @param newItems 要添加的更新项数组，不能为null
     * @throws IllegalArgumentException 如果newItems为null
     */
    public void addItems (Array<Table> newItems) {
        if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
        float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

        items.addAll(newItems);
        for (Table item : newItems) {
            addActor(item);
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
     * 获取所有项。
     *
     * @return 更新项
     */
    public Array<Table> getItems () {
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

    public TableListStyle getStyle() {
        return style;
    }

    public void setStyle(TableListStyle style) {
        this.style = style;
    }

    public static class TableListStyle {
        public Drawable background;

        public TableListStyle () {
        }

        public TableListStyle (Drawable background) {
            this.background = background;
        }

        public TableListStyle (TableListStyle style) {
            this.background = style.background;
        }
    }
}
