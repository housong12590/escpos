package com.hstmpl.escpos.enums;

/**
 * @author hous
 */
public enum Align {
    /**
     * 左对齐
     */
    left,
    /**
     * 居中对齐
     */
    center,
    /**
     * 右边齐
     */
    right;

    @Override
    public String toString() {
        return this.name();
    }

    public static Align of(String attribute, Align align) {
        if (attribute == null) {
            return align;
        }
        attribute = attribute.toLowerCase().trim();
        switch (attribute) {
            case "center":
                align = Align.center;
                break;
            case "right":
                align = Align.right;
                break;
            case "left":
            default:
                align = Align.left;
                break;
        }
        return align;
    }
}
