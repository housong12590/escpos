package com.cin.pos.element;


import com.cin.pos.Constants;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.StringUtil;
import com.cin.pos.util.Util;

import java.util.List;
import java.util.Map;

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
    public void parser(AttributeSet attrs, Map<String, Object> data) {
        super.parser(attrs, data);
        this.bold = attrs.getBooleanValue("bold", this.bold);
        this.underline = attrs.getBooleanValue("underline", this.underline);
        this.align = Align.parserAlign(attrs.getAttributeValue("align"), this.align);
        this.size = parserTextSize(attrs.getAttributeValue("size"), this.size);
        this.value = attrs.getAttributeValue("value", this.value);
        this.repeat = parserRepeat(attrs.getAttributeValue("repeat"), this.repeat);
        this.value = StringUtil.subExpression(data, Constants.REPLACE_PATTERN2, this.value);
        // 1,1,1,1 大小,加粗,对齐,下划线
//        String style = attrs.getAttributeValue("style");

    }


    public enum Size {
        normal(1), big(2), oversized(3);

        public int value;

        Size(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.name();
        }
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
                int count = Util.toInt(attribute, 0);
                repeat = Repeat.count;
                repeat.value = count;
                break;
        }
        return repeat;
    }


}
