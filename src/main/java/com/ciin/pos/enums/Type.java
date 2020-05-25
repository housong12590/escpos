package com.ciin.pos.enums;

import com.ciin.pos.util.ImageCreator;

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
        for (Type t : Type.values()) {
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
