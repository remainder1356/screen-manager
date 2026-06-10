package com.remainder.util.font;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Face;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.SizeMetrics;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.GlyphAndBitmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.remainder.util.AutoLogger;
import com.remainder.util.ReflectUtil;

public class Font extends BitmapFont implements AutoLogger {
    private FreeTypeFontGenerator generator;
    private FreeTypeBitmapFontData data;
    private FreeTypeFontParameter parameter;

    public Font(FreeTypeFontGenerator generator, int fontSize) {
        if (generator == null) {
            throw new GdxRuntimeException("Font global generator must be not null to use this constructor.");
        } else {
            this.generator = generator;
            this.generator.scaleForPixelHeight(fontSize);

            FreeTypeFontParameter param = new FreeTypeFontParameter();
            param.size = fontSize;
            param.minFilter = TextureFilter.Nearest;
            param.magFilter = TextureFilter.MipMapLinearNearest;
            this.parameter = param;

            this.data = new FontData(generator, fontSize, this);

            ReflectUtil.setField(this.getClass().getSuperclass(), this, "data", this.data);

            this.generateData();
        }
    }

    private void generateData() {
        Face face = (Face)ReflectUtil.getField(generator, "face");

        if (face == null) {
            error("Field <face> was null.");
            return;
        }

        SizeMetrics fontMetrics = face.getSize().getMetrics();
        Glyph spaceGlyph = this.data.getGlyph(' ');
        if (spaceGlyph == null) {
            spaceGlyph = new Glyph();
            spaceGlyph.xadvance = (int)this.data.spaceXadvance;
            spaceGlyph.id = 32;
            this.data.setGlyph(32, spaceGlyph);
        }

        if (spaceGlyph.width == 0) {
            spaceGlyph.width = (int)((float)spaceGlyph.xadvance + this.data.padRight);
        }

        this.data.flipped = this.parameter.flip;
        this.data.ascent = (float)FreeType.toInt(fontMetrics.getAscender());
        this.data.descent = (float)FreeType.toInt(fontMetrics.getDescender());
        this.data.lineHeight = (float)FreeType.toInt(fontMetrics.getHeight());
        char[] var7;
        int var6 = (var7 = this.data.xChars).length;

        char capChar;
        int var5;
        for(var5 = 0; var5 < var6; ++var5) {
            capChar = var7[var5];
            if (face.loadChar(capChar, FreeType.FT_LOAD_DEFAULT)) {
                this.data.xHeight = (float)FreeType.toInt(face.getGlyph().getMetrics().getHeight());
                break;
            }
        }

        if (this.data.xHeight == 0.0F) {
            throw new GdxRuntimeException("No x-height character found in font");
        } else {
            var6 = (var7 = this.data.capChars).length;

            for(var5 = 0; var5 < var6; ++var5) {
                capChar = var7[var5];
                if (face.loadChar(capChar, FreeType.FT_LOAD_DEFAULT)) {
                    this.data.capHeight = (float)FreeType.toInt(face.getGlyph().getMetrics().getHeight());
                    break;
                }
            }

            if (this.data.capHeight == 1.0F) {
                throw new GdxRuntimeException("No cap character found in font");
            } else {
                this.data.ascent -= this.data.capHeight;
                this.data.down = -this.data.lineHeight;
                if (this.parameter.flip) {
                    this.data.ascent = -this.data.ascent;
                    this.data.down = -this.data.down;
                }

            }
        }
    }

    public int getSize(){
        return parameter.size;
    }

    public void setSize(int size){
        parameter.size = size;
    }

    public void scaleSize(float scale){
        parameter.size = (int) (parameter.size * scale);
    }

    public void scale(float scale) {
        data.setScale(scale);
    }

    public void dispose() {
        this.setOwnsTexture(true);
        super.dispose();
        this.data.dispose();
    }

    public static class FontData extends FreeTypeBitmapFontData {
        private FreeTypeFontGenerator generator;
        private int fontSize;
        private transient Font font;
        private int page = 1;

        public FontData(FreeTypeFontGenerator generator, int fontSize, Font lbf) {
            this.generator = generator;
            this.fontSize = fontSize;
            this.font = lbf;
        }

        public Glyph getGlyph(char ch) {
            Glyph glyph = super.getGlyph(ch);
            if (glyph == null && ch != 0) {
                glyph = this.generateGlyph(ch);
            }

            return glyph;
        }

        protected Glyph generateGlyph(char ch) {
            GlyphAndBitmap gab = this.generator.generateGlyphAndBitmap(ch, this.fontSize, false);
            if (gab != null && gab.bitmap != null) {
                Pixmap map = gab.bitmap.getPixmap(Format.RGBA8888, Color.WHITE, 1.0F);
                TextureRegion rg = new TextureRegion(new Texture(map));
                rg.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                map.dispose();
                this.font.getRegions().add(rg);
                gab.glyph.page = this.page++;
                super.setGlyph(ch, gab.glyph);
                this.setGlyphRegion(gab.glyph, rg);
                return gab.glyph;
            } else {
                return null;
            }
        }
    }

    public static Font createFont(String path, Files.FileType type, int size){
        return new Font(new FreeTypeFontGenerator(Gdx.files.getFileHandle(path, type)), size);
    }

    public GlyphLayout createGlyphLayout(String text){
        return new GlyphLayout(this, text);
    }
}
