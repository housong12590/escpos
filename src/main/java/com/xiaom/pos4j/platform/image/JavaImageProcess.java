package com.xiaom.pos4j.platform.image;


import com.xiaom.pos4j.element.Image;
import com.xiaom.pos4j.util.ImageCreator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hous
 */
public class JavaImageProcess implements ImageProcess {

    @Override
    public Image imagePixelsFromFilePath(String pathName, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File(pathName));
            int[] pixels = ImageCreator.bufferImageToPixels(image);
            return new Image(image.getWidth(), image.getHeight(), pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Image imagePixelsFromInputStream(InputStream is, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(is);
            int[] pixels = ImageCreator.bufferImageToPixels(image);
            return new Image(image.getWidth(), image.getHeight(), pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
