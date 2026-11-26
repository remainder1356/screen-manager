package util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class TextUtil {
    public static float getTextWidth(BitmapFont font, String text){
        Pool<GlyphLayout> pool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = pool.obtain();
        layout.setText(font, text);
        float width = layout.width;
        pool.free(layout);
        return width;
    }

    public static float getTextHeight(BitmapFont font, String text){
        Pool<GlyphLayout> pool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = pool.obtain();
        layout.setText(font, text);
        float height = layout.height;
        pool.free(layout);
        return height;
    }

    public static int getTextBeyond(BitmapFont font, String text, float width){
        Pool<GlyphLayout> pool = Pools.get(GlyphLayout.class);
        GlyphLayout layout = pool.obtain();
        layout.setText(font, text);

        int pos = text.length();
        do {
            layout.setText(font, text.substring(0, pos));
            pos--;
        } while (layout.width > width);
        pool.free(layout);

        return pos;
    }

    public static String getTextInside(BitmapFont font, String text, float width){
        int pos = getTextBeyond(font, text, width);

        if(pos == text.length()) return text;

        return text.substring(0, pos);
    }
}
