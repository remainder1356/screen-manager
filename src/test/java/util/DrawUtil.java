package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class DrawUtil {
    public static void drawCentre(Batch batch, Texture texture, Rectangle container){
        batch.draw(texture, container.x + (container.width - texture.getWidth()) / 2, container.y + (container.height - texture.getHeight()) / 2);
    }

    public static void drawCentre(Batch batch, Texture texture, float x, float y, float width, float height){
        batch.draw(texture, x + (width - texture.getWidth()) / 2, y + (height - texture.getHeight()) / 2);
    }

    public static Texture rect(float width, float height, Color background, boolean edge) {
        Pixmap pixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);

        pixmap.setColor(background);
        pixmap.fillRectangle(0, 0, (int) width, (int) height);

        if (edge){
            pixmap.setColor(Color.BLACK);
            pixmap.drawRectangle(0, 0, (int) width, (int) height);
        }

        Texture t = new Texture(pixmap);

        pixmap.dispose();

        return t;
    }

    public static Texture roundRect(Color color, int width, int height, int radius) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                if (x <= radius && y <= radius) { // bottom-left
                    if (Math.sqrt((radius - x) * (radius - x) + (radius - y) * (radius - y)) > radius) {
                        continue;
                    }
                } else {
                    int t1 = ((height - radius) - y) * ((height - radius) - y);
                    if (x <= radius && y >= height - radius && y <= height) { // top-left
                        if (Math.sqrt((radius - x) * (radius - x) + t1) > radius) {
                            continue;
                        }
                    } else {
                        int t2 = ((width - radius) - x) * ((width - radius) - x);
                        if (x >= width - radius && x <= width && y <= radius) {// bottom-right
                            if (Math.sqrt(t2 + (radius - y) * (radius - y)) > radius) {
                                continue;
                            }
                        } else if ((x >= (width - radius) && x <= width) && (y >= (height - radius) && y <= height)) {// top-right
                            if (Math.sqrt(t2 + t1) > radius) {
                                continue;
                            }
                        }
                    }
                }
                pixmap.drawPixel(x, y);
            }
        }
        Texture t = new Texture(pixmap);
        pixmap.dispose();
        return t;
    }
}
