package com.xiaom.pos4j.orderset;

import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;

public class CloudPrintOrderSet implements OrderSet {

    public static final byte[] EMPTY_ARR = new byte[0];

    //<BR>：换行符（同一行有闭合标签(如 </C>)则应放到闭合标签前面, 连续两个换行符<BR><BR>可以表示加一空行）
    // <L></L>：左对齐
    // <C></C>：居中对齐
    // <R></R>：右对齐
    // <N></N>：字体正常大小
    // <HB></HB>：字体变高一倍
    // <WB></WB>：字体变宽一倍
    // <B></B>：字体放大一倍
    // <CB></CB>：字体放大一倍居中
    // <HB2></HB2>：字体变高二倍
    // <WB2></WB2>：字体变宽二倍
    // <B2></B2>：字体放大二倍
    // <BOLD></BOLD>：字体加粗
    // <LOGO></LOGO>：LOGO图片（标签内容是图片Base64格式字符串, 暂未开放）
    // <QR></QR>：二维码（标签内容是二维码值, 最大不能超过256个字符）
    // <BARCODE></BARCODE>：条形码（标签内容是条形码值）
    // <CUT>：切刀指令（主动切纸，仅限于切刀打印机使用才有效果。注意：切刀打印机的打印订单最后默认带一个切刀指令）

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
            case LEFT:
                return L;
            case RIGHT:
                return R;
            case CENTER:
                return C;
        }
        return new byte[0];
    }

    @Override
    public byte[] bold(boolean bold) {
//        if (bold) {
//            return BOLD;
//        }
//        return "</BOLD>".getBytes();

        return EMPTY_ARR;
    }

    @Override
    public byte[] underline(boolean underline) {
        return EMPTY_ARR;
    }

    @Override
    public byte[] textSize(Size size) {
        switch (size) {
            case normal:
            case w1h1:
                return N;
            case big:
            case w2h2:
                return B;
            case w1h2:
                return HB;
            case w2h1:
                return WB;
            case w3h3:
            case oversized:
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