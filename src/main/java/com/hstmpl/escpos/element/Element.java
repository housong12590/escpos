package com.hstmpl.escpos.element;

/**
 * @author hous
 */
public abstract class Element {

    private int[] margin = new int[4];
    private boolean newLine = true;

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
}
