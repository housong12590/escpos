package com.ciin.pos.element;


import com.ciin.pos.common.Dict;
import com.ciin.pos.element.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ImageCreator;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

public class Image extends Element {

    private Type type = Type.image;
    private String value = "";
    private int[] pixels;
    private Align align = Align.left;

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
        image, qrcode, barcode;

        @Override
        public String toString() {
            return this.name();
        }
    }


    @Override
    public void parser(AttributeSet attrs, Dict data) throws TemplateParseException {
        super.parser(attrs, data);
        this.value = attrs.getAttributeValue("value", this.value);
        this.type = parserImageType(attrs.getAttributeValue("type"), this.type);
        this.align = Align.parserAlign(attrs.getAttributeValue("align"), this.align);
        if (StringUtils.isNotEmpty(this.value)) {
            if (getWidth() > 0 && getHeight() > 0) {
                if (type == Image.Type.image) {
                    this.pixels = ImageCreator.createImagePixels(this.value, getWidth(), getHeight());
                } else if (type == Image.Type.qrcode) {
                    this.pixels = ImageCreator.createQrCodePixels(this.value, getWidth(), getHeight());
                } else if (type == Image.Type.barcode) {
                    this.pixels = ImageCreator.createBarcodePixels(this.value, getWidth(), getHeight());
                }
                if (this.pixels == null) {
                    LogUtils.error("image pixels can not null !!!");
                }
            }
        } else {
            LogUtils.error("image value can not null !!!");
        }

    }


    private Type parserImageType(String attribute, Type type) {
        if (attribute == null) {
            return type;
        }
        attribute = attribute.toLowerCase().trim();
        switch (attribute) {
            case "image":
                type = Type.image;
                break;
            case "qrcode":
                type = Type.qrcode;
                break;
            case "barcode":
                type = Type.barcode;
                break;
        }
        return type;
    }
}
