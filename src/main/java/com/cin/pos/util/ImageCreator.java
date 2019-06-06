package com.cin.pos.util;

import com.cin.pos.element.Image;
import com.cin.pos.platform.Platform;
import com.cin.pos.platform.image.ImageProcess;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageCreator {

    private static final int BLACK = 0xff000000;

    private static final int WHITE = 0Xffffffff;

    public static int[] createQrCodePixels(String value, int width, int height) {
        checkClassExist();
        Map<EncodeHintType, Object> params = new HashMap<>();
        params.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        params.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(value, BarcodeFormat.QR_CODE, width, height, params);
            return matrixToPixels(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] createBarcodePixels(String value, int width, int height) {
        checkClassExist();
        Map<EncodeHintType, Object> params = new HashMap<>();
        params.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        params.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(value, BarcodeFormat.CODE_128, width, height, params);
            return matrixToPixels(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkClassExist() {
        try {
            Class.forName("com.google.zxing.MultiFormatWriter");
        } catch (ClassNotFoundException e) {
            throw new NullPointerException("zxing not exist");
        }
    }


    public static int[] createImagePixels(String value, int width, int height) {
        Platform platform = Platform.get();
        ImageProcess imageProcess = platform.getImageProcess();
        boolean matches = value.matches("^(http|https)://.*?");
        Image image = null;
        if (matches) {
            try {
                URL url = new URL(value);
                InputStream is = url.openStream();
                image = imageProcess.imagePixelsFromInputStream(is, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            image = imageProcess.imagePixelsFromFilePath(value, width, height);
        }
        int[] pixels = null;
        if (image != null) {
            pixels = image.getPixels();
            if (image.getWidth() != width || image.getHeight() != height) {
                pixels = scalePixels(image.getPixels(), image.getWidth(), image.getHeight(), width, height);
            }
        }
        return pixels;
    }

    private static int[] scalePixels(int[] srcPixels, int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        double wRate, hRate;
        wRate = (double) srcWidth / dstWidth;
        hRate = (double) srcHeight / dstHeight;
        int[] dstPixels = new int[dstWidth * dstHeight];
        for (int y = 0; y < dstHeight; y++) {
            for (int x = 0; x < dstWidth; x++) {
                int i = (int) (y * hRate) * (int) (dstWidth * wRate) + (int) (x * wRate);
                dstPixels[y * dstWidth + x] = srcPixels[i];
            }
        }
        return dstPixels;
    }


    private static int[] matrixToPixels(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        return pixels;
    }

    public static int[] bufferImageToPixels(BufferedImage bi) {
        if (bi != null) {
            int w = bi.getWidth();
            int h = bi.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = bi.getRGB(x, y);
                    pixels[y * w + x] = pixel;
                }
            }
            return pixels;
        }
        return null;
    }

    /**
     * 判断一个像素点是黑色还是白色，这里用了简化计算，没有采用灰度公式
     *
     * @param pixel 单个像素点
     */
    private static boolean assertBlack(int pixel) {
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        int gray = (red * 306 + green * 601 + blue * 117) >> 10;
        return gray < 160;
    }

    /**
     * 将24位色图转化为单色位图
     *
     * @param width
     * @param height
     * @param srcPixels
     * @param dstPixels
     */
    public static void convertMultiBytesToSingleBit(int width, int height, int[] srcPixels, byte[] dstPixels) {
        int index = 0;
        for (int y = 0; y < height; y++) {
            byte one_byte_with_eight_dot = 0x00;
            int position = 7;
            for (int x = 0; x < width; x++) {
                int one_pixel_with_mulity_bit = srcPixels[y * width + x];
                if (assertBlack(one_pixel_with_mulity_bit)) {
                    one_byte_with_eight_dot |= 1 << position;// 把该位置1
                }
                position--;
                if (position < 0) {
                    dstPixels[index++] = one_byte_with_eight_dot;
                    one_byte_with_eight_dot = 0x00;
                    position = 7;
                }
            }
            if (width % 8 > 0) {
                dstPixels[index++] = one_byte_with_eight_dot;
            }
        }
    }

}
