package com.ciin.pos.element;

import com.ciin.pos.Constants;
import com.ciin.pos.common.Dict;
import com.ciin.pos.exception.TemplateException;
import com.ciin.pos.exception.UnsatisfiedConditionException;
import com.ciin.pos.parser.Parser;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ConvertUtils;
import com.ciin.pos.util.ExpressionUtils;
import com.ciin.pos.util.StringUtils;

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
    public void parser(AttributeSet attrs, Dict data) throws TemplateException {
        if (!attrs.hasAttribute("condition")) {
            String condition = attrs.getAttributeValue("condition", null);
            if (!checkCondition(data, condition)) {
                // 条件不满足 不进行解析
                String errorMsg = String.format("%s %s 判断条件不成立,退出解析过程", this.getClass(), condition);
                throw new UnsatisfiedConditionException(errorMsg);
            }
        }

        this.width = attrs.getIntValue("width", WARP_CONTENT);
        this.height = attrs.getIntValue("height", WARP_CONTENT);

        parserMargin(attrs, margin);
    }

    private void parserMargin(AttributeSet attrs, int[] margin) {
        String value = attrs.getAttributeValue("margin");
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
        margin[0] = attrs.getIntValue("marginLeft", margin[0]);
        margin[1] = attrs.getIntValue("marginTop", margin[1]);
        margin[2] = attrs.getIntValue("marginRight", margin[2]);
        margin[3] = attrs.getIntValue("marginBottom", margin[3]);
    }

    private boolean checkCondition(Dict data, String condition) {
        if (data == null || StringUtils.isEmpty(condition)) {
            return true;
        }
        if (condition.matches("(true|false)")) {
            return Boolean.parseBoolean(condition);
        }
        String placeholderKey = ExpressionUtils.getExpression(Constants.PARSE_PATTERN, condition);
        if (!StringUtils.isEmpty(placeholderKey)) {
            Object value = data.getExpressionValue(placeholderKey);
            if (value == null) {
                return false;
            } else {
                return value instanceof Boolean ? (Boolean) value : true;
            }
        }
        return false;
    }
}
