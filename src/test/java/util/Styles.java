package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.remainder.util.font.DefaultFont;

public class Styles {
    public static final Label.LabelStyle labelStyle = VisUI.getSkin().get(Label.LabelStyle.class);

    public static final TextTooltip.TextTooltipStyle tooltipStyle = new TextTooltip.TextTooltipStyle();

    public static final VisCheckBox.VisCheckBoxStyle checkBoxStyle = VisUI.getSkin().get(VisCheckBox.VisCheckBoxStyle.class);

    public static final TextButton.TextButtonStyle textButtonStyle = VisUI.getSkin().get(TextButton.TextButtonStyle.class);

    public static final VisTextButton.VisTextButtonStyle buttonStyle = VisUI.getSkin().get(VisTextButton.VisTextButtonStyle.class);

    public static final VisImageTextButton.VisImageTextButtonStyle visImageTextButtonStyle = VisUI.getSkin().get(VisImageTextButton.VisImageTextButtonStyle.class);

    public static final ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = VisUI.getSkin().get(ImageTextButton.ImageTextButtonStyle.class);

    public static final List.ListStyle listStyle = VisUI.getSkin().get(List.ListStyle.class);

    public static void init() {
        labelStyle.font = DefaultFont.getFont();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.background = new TextureRegionDrawable(DrawUtil.rect(10, 10, Color.GRAY, false));

        tooltipStyle.background = null;
        tooltipStyle.wrapWidth = 0;
        tooltipStyle.label = labelStyle;

        checkBoxStyle.font = DefaultFont.getFont();

        textButtonStyle.font = DefaultFont.getFont();

        buttonStyle.font = DefaultFont.getFont();

        visImageTextButtonStyle.font = DefaultFont.getFont();
        imageTextButtonStyle.font = DefaultFont.getFont();

        listStyle.font = DefaultFont.getFont();
    }
}
