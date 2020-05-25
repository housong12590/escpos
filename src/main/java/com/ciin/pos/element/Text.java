package com.ciin.pos.element;


import com.ciin.pos.Constants;
import com.ciin.pos.common.Dict;
import com.ciin.pos.enums.Align;
import com.ciin.pos.enums.Repeat;
import com.ciin.pos.enums.Size;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ExpressionUtils;

public class Text extends Element {

    private String value = "";
    private Repeat repeat = Repeat.none;
    private Align align = Align.LEFT;
    private Size size = Size.normal;
    private boolean bold;
    private boolean underline;

    public Text() {
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public void parser0(AttributeSet attrs, Dict data) throws TemplateParseException {
        this.bold = attrs.getBooleanValue(Attribute.BOLD, this.bold);
        this.underline = attrs.getBooleanValue(Attribute.UNDERLINE, this.underline);
        this.align = Align.of(attrs.getAttributeValue(Attribute.ALIGN), this.align);
        this.size = Size.of(attrs.getAttributeValue(Attribute.SIZE), this.size);
        this.value = attrs.getAttributeValue(Attribute.VALUE, this.value);
        this.repeat = Repeat.of(attrs.getAttributeValue(Attribute.REPEAT), this.repeat);
        this.value = ExpressionUtils.replacePlaceholder(Constants.PARSE_PATTERN, this.value, data);
    }
}
