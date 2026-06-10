package com.remainder.util.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class DefaultFont {
    private static final DefaultFont instance = new DefaultFont();

    private FileHandle fontFile = Gdx.files.classpath("font.ttf");
    public Font font;

    private DefaultFont() {
        init();
    }

    /**
     * Sets the font file to be used and reinitialized the font resources.
     * 
     * @param fontFile the file handle for the new font file
     */
    public void setFontFile(FileHandle fontFile) {
        this.fontFile = fontFile;
        init();
    }

    /**
     * Gets the default font instance. If the font hasn't been initialized,
     * it initializes the font first.
     * 
     * @return the default font instance
     */
    public static Font getFont(){
        if (instance.font == null) {
            instance.init();
        }

        return instance.font;
    }

    /**
     * Creates a new font instance with the specified size.
     * 
     * @param size the size of the font to create
     * @return a new font instance with the given size
     */
    public static Font createFont(FileHandle fontFile, int size) {
        return new Font(new FreeTypeFontGenerator(fontFile), size);
    }

    /**
     * Initializes the font resources. This method loads the font file and creates
     * a default font instance with size 16. If the font file doesn't exist,
     * an error is logged.
     */
    public void init() {
        if (!fontFile.exists()) {
            Gdx.app.error("DefaultFont", "<font.ttf> not found");
            return ;
        }

        font = createFont(fontFile, 16);
    }

    /**
     * Disposes of the font resources.
     * This method disposes of both the font and the generator.
     */
    public void dispose() {
        if (font != null) {
            font.dispose();
            font = null;
        }
    }
}
