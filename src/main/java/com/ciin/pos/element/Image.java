package com.ciin.pos.element;


import com.ciin.pos.common.Dict;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ImageCreator;
import com.ciin.pos.util.StringUtils;

public class Image extends Element {

    private Type type = Type.image;
    private String value = "";
    private int[] pixels;
    private Align align = Align.LEFT;

    public Image() {

    }

    public Image(int width, int height, int[] pixels) {
        this.pixels = pixels;
        setWidth(width);
        setHeight(height);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }


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

        @Override
        public String toString() {
            return this.name();
        }

        public static Type of(String value) {
            for (Type type : Type.values()) {
                if (type.name().equals(value)) {
                    return type;
                }
            }
            return image;
        }
    }

    @Override
    public void parser0(AttributeSet attrs, Dict data) throws TemplateParseException {
        this.value = attrs.getAttributeValue(Attribute.VALUE, this.value);
        this.type = parserImageType(attrs.getAttributeValue(Attribute.TYPE), this.type);
        this.align = Align.parserAlign(attrs.getAttributeValue(Attribute.ALIGN), this.align);
        if (StringUtils.isEmpty(this.value)) {
            throw new TemplateParseException("image value can not null");
        }
        if (getWidth() > 0 && getHeight() > 0) {
            switch (type) {
                case image:
                    this.pixels = ImageCreator.createImagePixels(this.value, getWidth(), getHeight());
                    break;
                case qrcode:
                    this.pixels = ImageCreator.createQrCodePixels(this.value, getWidth(), getHeight());
                    break;
                case barcode:
                    this.pixels = ImageCreator.createBarcodePixels(this.value, getWidth(), getHeight());
                    break;
            }
            if (this.pixels == null) {
                throw new TemplateParseException("image pixels can not null !");
            }
        }
    }


    private Type parserImageType(String attribute, Type type) {
        if (attribute == null) {
            return type;
        }
        return Type.of(attribute.toLowerCase().trim());
    }
}
