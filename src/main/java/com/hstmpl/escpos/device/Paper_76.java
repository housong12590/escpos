package com.hstmpl.escpos.device;

/**
 * @author hous
 */
public class Paper_76 implements Paper {
    @Override
    public int paperWidth() {
        return 76;
    }

    @Override
    public int getCharLen() {
        return 40;
    }

    @Override
    public int getPixel() {
        return 78 * 8;
    }
}
