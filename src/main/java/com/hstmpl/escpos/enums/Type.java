package com.hstmpl.escpos.enums;

import com.hstmpl.escpos.element.Image;
import com.hstmpl.escpos.util.ImageCreator;

/**
 * @author hous
 */
public enum Type {

    /**
     * 图片
     */
    image,
    /**
     * 二维码
     */
    qrcode,
    /**
     * 条形码
     */
    barcode;

    private static final Type[] ENUMS = Type.values();


    public int[] createPixels(Image image) {
        return createPixels(image.getValue(), image.getWidth(), image.getHeight());
    }

    public int[] createPixels(String value, int width, int height) {
        switch (this) {
            case image:
                return ImageCreator.createImagePixels(value, width, height);
            case qrcode:
                return ImageCreator.createQrCodePixels(value, width, height);
            case barcode:
                return ImageCreator.createBarcodePixels(value, width, height);
        }
        return null;
    }



    public static Type of(String value, Type type) {
        for (Type t : ENUMS) {
            if (t.name().equals(value)) {
                return t;
            }
        }
        return type;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
