package com.hstmpl.escpos.platform.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.hstmpl.escpos.element.Image;

import java.io.InputStream;

/**
 * @author hous
 */
public class AndroidImageProcess implements ImageProcess {

    @Override
    public Image imagePixelsFromFilePath(String pathName, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                pixels[y * width + x] = pixel;
            }
        }
        return new Image(w, h, pixels);
    }

    @Override
    public Image imagePixelsFromInputStream(InputStream is, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                pixels[y * width + x] = pixel;
            }
        }
        return new Image(w, h, pixels);
    }
}
