package com.hstmpl.escpos.enums;

/**
 * @author hous
 */
public enum Size {

    w1h1(1, 1),
    w2h1(2, 1),
    w3h1(3, 1),
    w1h2(1, 2),
    w1h3(1, 3),
    w2h3(2, 3),
    w2h2(2, 2),
    w3h3(3, 3),
    w3h2(3, 2);

    public int w;

    public int h;

    Size(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return this.name();
    }


    public static Size of(String value, Size size) {
        if (value == null) {
            return size;
        }
        switch (value) {
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
            default:
                size = Size.w1h1;
                break;
        }
        return size;
    }
}
