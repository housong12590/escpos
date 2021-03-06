package com.ciin.pos.element;


import com.ciin.pos.Constants;
import com.ciin.pos.common.Dict;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ConvertUtils;
import com.ciin.pos.util.ExpressionUtils;

import java.util.List;

public class Text extends Element {

    private String value = "";
    private Repeat repeat = Repeat.none;
    private Align align = Align.left;
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
    public void parser(AttributeSet attrs, Dict data) throws TemplateParseException {
        super.parser(attrs, data);
        this.bold = attrs.getBooleanValue("bold", this.bold);
        this.underline = attrs.getBooleanValue("underline", this.underline);
        this.align = Align.parserAlign(attrs.getAttributeValue("align"), this.align);
        this.size = parserTextSize(attrs.getAttributeValue("size"), this.size);
        this.value = attrs.getAttributeValue("value", this.value);
        this.repeat = parserRepeat(attrs.getAttributeValue("repeat"), this.repeat);
        this.value = ExpressionUtils.replacePlaceholder(Constants.PARSE_PATTERN, this.value, data);
    }


    public enum Repeat {
        none, auto, fill, count;

        public int value;

        public List<Text> texts;


        @Override
        public String toString() {
            return this.name();
        }
    }

    private Size parserTextSize(String attribute, Size size) {
        if (attribute == null) {
            return size;
        }
        attribute = attribute.toLowerCase().trim();
        switch (attribute) {
            case "normal":
            case "1":
            case "x1":
                size = Size.normal;
                break;
            case "big":
            case "2":
            case "x2":
                size = Size.big;
                break;
            case "oversized":
            case "3":
            case "x3":
                size = Size.oversized;
                break;
            case "w1h1":
                size = Size.w1h1;
                break;
            case "w2h1":
                size = Size.w2h1;
                break;
            case "w3h1":
                size = Size.w3h1;
                break;
            case "w1h2":
                size = Size.w1h2;
                break;
            case "w1h3":
                size = Size.w1h3;
                break;
            case "w2h2":
                size = Size.w2h2;
                break;
            case "w2h3":
                size = Size.w2h3;
                break;
            case "w3h2":
                size = Size.w3h2;
                break;
            case "w3h3":
                size = Size.w3h3;
                break;
        }
        return size;
    }


    private Repeat parserRepeat(String attribute, Repeat repeat) {
        if (attribute == null || attribute.equals("")) {
            return repeat;
        }
        attribute = attribute.toLowerCase().trim();
        switch (attribute) {
            case "none":
                repeat = Repeat.none;
                break;
            case "auto":
                repeat = Repeat.auto;
                break;
            case "fill":
                repeat = Repeat.fill;
                break;
            default:
                int count = ConvertUtils.toInt(attribute, 0);
                repeat = Repeat.count;
                repeat.value = count;
                break;
        }
        return repeat;
    }


}
