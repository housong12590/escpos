package com.ciin.pos.element;


import com.ciin.pos.common.Dict;
import com.ciin.pos.enums.Align;
import com.ciin.pos.enums.Type;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
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


    @Override
    public void parser0(AttributeSet attrs, Dict data) throws TemplateParseException {
        this.value = attrs.getAttributeValue(Attribute.VALUE, this.value);
        this.type = Type.of(attrs.getAttributeValue(Attribute.TYPE), this.type);
        this.align = Align.of(attrs.getAttributeValue(Attribute.ALIGN), this.align);
        if (StringUtils.isEmpty(this.value)) {
            throw new TemplateParseException("image value can not null");
        }
        if (getWidth() > 0 && getHeight() > 0) {
            this.pixels = type.createPixels(value, getWidth(), getHeight());
            if (this.pixels == null) {
                throw new TemplateParseException("image pixels can not null !");
            }
        }
    }
}
