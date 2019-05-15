package com.cin.pos.element;

import com.cin.pos.element.exception.ConditionNotExistException;
import com.cin.pos.Constants;
import com.cin.pos.parser.Parser;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.StringUtil;
import com.cin.pos.util.Util;

import java.util.Map;
import java.util.regex.Matcher;

public abstract class Element implements Parser {

    public static final int WARP_CONTENT = -1;
    private int width;
    private int height;
    private int[] margin = new int[4];
    private boolean newLine = true;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getMargin() {
        return margin;
    }

    public void setMargin(int[] margin) {
        this.margin = margin;
    }

    public int getMarginLeft() {
        return this.margin[0];
    }

    public int getMarginTop() {
        return this.margin[1];
    }

    public int getMarginRight() {
        return this.margin[2];
    }

    public int getMarginBottom() {
        return this.margin[3];
    }

    public boolean isNewLine() {
        return newLine;
    }

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }

    @Override
    public void parser(AttributeSet attrs, Map<String, Object> data) {
        String condition = attrs.getAttributeValue("condition", "");
        // 条件不满足 不进行解析
        if (!checkCondition(data, condition)) {
            throw new ConditionNotExistException(String.format("%s  %s 判断条件不成立 ,退出解析过程", this.getClass(),condition));
        }
        this.width = attrs.getIntValue("width", WARP_CONTENT);
        this.height = attrs.getIntValue("height", WARP_CONTENT);

        parserMargin(attrs, margin);
    }

    private void parserMargin(AttributeSet attrs, int[] margin) {
        String value = attrs.getAttributeValue("margin");
        if (StringUtil.isNotEmpty(value)) {
            String[] splits = value.split(",");
            int[] margins = new int[splits.length];
            for (int i = 0; i < splits.length; i++) {
                margins[i] = Util.toInt(splits[i]);
            }
            int left, top, right, bottom;
            if (margins.length == 1) {
                left = top = right = bottom = margins[0];
            } else if (margins.length == 2) {
                left = margins[0];
                right = margins[1];
                top = bottom = 0;
            } else if (margins.length == 3) {
                left = margins[0];
                top = margins[1];
                right = margins[2];
                bottom = 0;
            } else {
                left = margins[0];
                top = margins[1];
                right = margins[2];
                bottom = margins[3];
            }
            margin[0] = left;
            margin[1] = top;
            margin[2] = right;
            margin[3] = bottom;
        }
        String marginLeft = attrs.getAttributeValue("marginLeft");
        if (StringUtil.isNotEmpty(marginLeft)) {
            margin[0] = Util.toInt(marginLeft, margin[0]);
        }
        String marginTop = attrs.getAttributeValue("marginTop");
        if (StringUtil.isNotEmpty(marginTop)) {
            margin[1] = Util.toInt(marginTop, margin[1]);
        }
        String marginRight = attrs.getAttributeValue("marginRight");
        if (StringUtil.isNotEmpty(marginRight)) {
            margin[2] = Util.toInt(marginRight, margin[2]);
        }
        String marginBottom = attrs.getAttributeValue("marginBottom");
        if (StringUtil.isNotEmpty(marginBottom)) {
            margin[3] = Util.toInt(marginBottom, margin[3]);
        }
    }

    private boolean checkCondition(Map data, String condition) {
        if (StringUtil.isEmpty(condition)) {
            return true;
        }
        if (condition.equals("true") || condition.equals("false")) {
            return Boolean.parseBoolean(condition);
        }
        Matcher matcher = Constants.REPLACE_PATTERN2.matcher(condition);
        if (matcher.find()) {
            String key = matcher.group(1);
            Object value = data.get(key);
            if (value == null) {
                return false;
            } else {
                return value instanceof Boolean ? (Boolean) value : true;
            }
        }
        return false;
    }
}
