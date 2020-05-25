package com.ciin.pos.enums;

public enum Align {
    /**
     * 左对齐
     */
    LEFT,
    /**
     * 居中对齐
     */
    CENTER,
    /**
     * 右边齐
     */
    RIGHT;

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
            case "1":
            case "c":
                align = Align.CENTER;
                break;
            case "right":
            case "2":
            case "r":
                align = Align.RIGHT;
                break;
            case "left":
            case "0":
            case "l":
            default:
                align = Align.LEFT;
                break;
        }
        return align;
    }
}
