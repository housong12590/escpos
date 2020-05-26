package com.xiaom.pos4j.element;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.Parser;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.ConvertUtils;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.util.Map;

/**
 * @author hous
 */
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
    public void parser(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {
        String condition = attrs.getAttributeValue(Attribute.CONDITION, null);
        if (StringUtils.isNotEmpty(condition) && !checkCondition(data, condition)) {
            // 条件不满足 不进行解析
            String errorMsg = String.format("%s %s 判断条件不成立,退出节点解析", this.getClass(), condition);
//            LogUtils.debug(errorMsg);
            return;
//            throw new UnsatisfiedConditionException(errorMsg);
        }

        this.width = attrs.getIntValue(Attribute.WIDTH, WARP_CONTENT);
        this.height = attrs.getIntValue(Attribute.HEIGHT, WARP_CONTENT);
        parserMargin(attrs, margin);
        parser0(attrs, data);
    }

    public abstract void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException;

    private void parserMargin(AttributeSet attrs, int[] margin) {
        String value = attrs.getAttributeValue(Attribute.MARGIN);
        if (StringUtils.isNotEmpty(value)) {
            String[] splits = value.split(",");
            int[] margins = new int[splits.length];
            for (int i = 0; i < splits.length; i++) {
                margins[i] = ConvertUtils.toInt(splits[i]);
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
        margin[0] = attrs.getIntValue(Attribute.MARGIN_LEFT, margin[0]);
        margin[1] = attrs.getIntValue(Attribute.MARGIN_TOP, margin[1]);
        margin[2] = attrs.getIntValue(Attribute.MARGIN_RIGHT, margin[2]);
        margin[3] = attrs.getIntValue(Attribute.MARGIN_BOTTOM, margin[3]);
    }

    private boolean checkCondition(Map<?, ?> data, String condition) {
        if (data == null || StringUtils.isEmpty(condition)) {
            return true;
        }
        if (condition.matches("(true|false)")) {
            return Boolean.parseBoolean(condition);
        }
        String placeholderKey = ExpressionUtils.getExpression(Constants.PARSE_PATTERN, condition);
        if (!StringUtils.isEmpty(placeholderKey)) {
            Object value = ExpressionUtils.getExpressionValue(data, placeholderKey);
            if (value == null) {
                return false;
            } else {
                return value instanceof Boolean ? (Boolean) value : true;
            }
        }
        return false;
    }
}
