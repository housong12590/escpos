package com.cin.pos.element;

public enum Align {

    left, center, right;

    @Override
    public String toString() {
        return this.name();
    }

    public static Align parserAlign(String attribute, Align align) {
        if (attribute == null) {
            return align;
        }
        attribute = attribute.toLowerCase().trim();
        switch (attribute) {
            case "left":
            case "0":
            case "l":
                align = Align.left;
                break;
            case "center":
            case "1":
            case "c":
                align = Align.center;
                break;
            case "right":
            case "2":
            case "r":
                align = Align.right;
                break;
        }
        return align;
    }
}
