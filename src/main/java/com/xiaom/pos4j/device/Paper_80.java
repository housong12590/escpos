package com.xiaom.pos4j.device;

/**
 * @author hous
 */
public class Paper_80 implements Paper {
    @Override
    public int paperWidth() {
        return 80;
    }

    @Override
    public int getCharLen() {
        return 48;
    }

    @Override
    public int getPixel() {
        return 80 * 8;
    }
}
