package com.xiaom.pos4j.element;


import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Type;

/**
 * @author hous
 */
public class Image extends Element {

    private Type type = Type.image;
    private int width;
    private int height;
    private String value = "";
    private int[] pixels;
    private Align align = Align.left;

    public Image() {

    }

    public Image(int width, int height, int[] pixels) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

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
}
