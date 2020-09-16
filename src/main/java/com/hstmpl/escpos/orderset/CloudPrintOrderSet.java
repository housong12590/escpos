package com.hstmpl.escpos.orderset;

import com.hstmpl.escpos.enums.Align;
import com.hstmpl.escpos.enums.Size;

/**
 * 云打印机指令集
 *
 * @author hous
 */
public class CloudPrintOrderSet implements OrderSet {

    public static OrderSet INSTANCE = new CloudPrintOrderSet();

    public static final byte[] EMPTY_ARR = new byte[0];

    public static final byte[] BR = "<BR>".getBytes();
    public static final byte[] L = "<L>".getBytes();
    public static final byte[] C = "<C>".getBytes();
    public static final byte[] R = "<R>".getBytes();
    public static final byte[] N = "<N>".getBytes();
    public static final byte[] B = "<B>".getBytes();
    public static final byte[] B2 = "<B2>".getBytes();
    public static final byte[] HB = "<HB>".getBytes();
    public static final byte[] WB = "<WB>".getBytes();
    public static final byte[] HB2 = "<HB2>".getBytes();
    public static final byte[] WB2 = "<WB2>".getBytes();
    public static final byte[] BOLD = "<BOLD>".getBytes();
    public static final byte[] UNBOLD = "</BOLD>".getBytes();
    public static final byte[] CUT = "<CUT>".getBytes();


    @Override
    public byte[] reset() {
        return UNBOLD;
    }

    @Override
    public byte[] buzzer(int count) {
        return EMPTY_ARR;
    }

    @Override
    public byte[] newLine() {
        return BR;
    }

    @Override
    public byte[] cutPaper() {
        return EMPTY_ARR;
    }

    @Override
    public byte[] status() {
        return EMPTY_ARR;
    }

    @Override
    public byte[] align(Align align) {
        switch (align) {
            case left:
                return L;
            case right:
                return R;
            case center:
                return C;
        }
        return new byte[0];
    }

    @Override
    public byte[] bold(boolean bold) {
        return EMPTY_ARR;
    }

    @Override
    public byte[] underline(boolean underline) {
        return EMPTY_ARR;
    }

    @Override
    public byte[] textSize(Size size) {
        switch (size) {
            case w1h1:
                return N;
            case w2h2:
                return B;
            case w1h2:
                return HB;
            case w2h1:
                return WB;
            case w3h3:
                return B2;
            case w1h3:
                return HB2;
            case w2h3:
                return "<WB><HB2>".getBytes();
            case w3h1:
                return WB2;
            case w3h2:
                return "<WB2><HB>".getBytes();
        }
        return EMPTY_ARR;
    }

    @Override
    public byte[] paperFeed(int n) {
        return newLine();
    }

    @Override
    public byte[] printImage(byte module, byte xL, byte xH, byte yL, byte yH, byte[] imageData) {
        return EMPTY_ARR;
    }

    @Override
    public byte[] printerInfo() {
        return EMPTY_ARR;
    }

    @Override
    public byte[] heartbeat() {
        return EMPTY_ARR;
    }
}
