package com.xiaom.pos4j.element;


import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Type;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.util.Map;

/**
 * @author hous
 */
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
    public void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {
        this.value = attrs.getAttributeValue(Attribute.VALUE, this.value);
        this.value = ExpressionUtils.replacePlaceholder(Constants.PARSE_PATTERN, this.value, data);
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
