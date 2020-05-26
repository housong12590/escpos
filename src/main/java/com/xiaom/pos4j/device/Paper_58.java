package com.xiaom.pos4j.device;

/**
 * @author hous
 */
public class Paper_58 implements Paper {
    @Override
    public int paperWidth() {
        return 58;
    }

    @Override
    public int getCharLen() {
        return 32;
    }

    @Override
    public int getPixel() {
        return 58 * 8;
    }
}
