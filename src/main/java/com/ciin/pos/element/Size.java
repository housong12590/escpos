package com.ciin.pos.element;

public enum Size {

    /**
     * 正常大小
     */
    normal(1, 1),
    /**
     * 较大
     */
    big(2, 2),
    /**
     * 超大
     */
    oversized(3, 3),
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
}
