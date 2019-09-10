package com.ciin.pos.orderset;


import com.ciin.pos.element.Align;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.element.Size;

/**
 * 佳博打印机指令集
 */
public class GprinterOrderSet implements OrderSet {

    private static final byte LF = 0x0a;
    private static final byte ESC = 0x1b;
    private static final byte GS = 0x1d;
    private static final byte FS = 0x1c;

    /* 初始化打印机 ECS @ */
    private static final byte[] ESC_RESET = new byte[]{ESC, 0x40};
    /* 打印并换行 */
    private static final byte[] ESC_NEW_LINE = new byte[]{LF};
    /* 切纸 */
    private static final byte[] ESC_CUTPAPER = new byte[]{ESC, 0x6d};
    /* 打印机品牌 */
    private static final byte[] PRINTER_BRAND = new byte[]{GS, 0x49, 0x42};
    /* 打印机状态 */
    private static final byte[] PRINTER_STATUS = new byte[]{GS, 0x72, 0x01};

    @Override
    public byte[] reset() {
        return ESC_RESET;
    }

    @Override
    public byte[] buzzer(int count) {
        if (count < 1) {
            count = 1;
        } else if (count > 9) {
            count = 9;
        }
        return new byte[]{ESC, 0x42, (byte) count, 0x01};
    }

    @Override
    public byte[] status() {
        return PRINTER_STATUS;
    }


    @Override
    public byte[] align(Align align) {
        byte b;
        if (align == Align.center) b = 0x01;
        else if (align == Align.right) b = 0x02;
        else b = 0x00;
        return new byte[]{ESC, 0x61, b};
    }

    @Override
    public byte[] bold(boolean bold) {
        return new byte[]{ESC, 0x45, (byte) (bold ? 0x01 : 0x00)};
    }

    @Override
    public byte[] underline(boolean underline) {
        return new byte[]{FS, 0x2d, (byte) (underline ? 0x01 : 0x00)};
    }

    @Override
    public byte[] textSize(Size size) {
        int h = size.h;
        int w = size.w;
        byte b = (byte) ((w == 2 ? 0x10 : w == 3 ? 0x20 : 0x00) | (h == 2 ? 0x01 : h == 3 ? 0x02 : 0x00));
        return new byte[]{GS, 0x21, b};
    }

    @Override
    public byte[] paperFeed(int n) {
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        return new byte[]{ESC, 0x64, (byte) n};
    }

    @Override
    public byte[] printImage(byte module, byte xL, byte xH, byte yL, byte yH, byte[] imageData) {
        ByteBuffer buffer = new ByteBuffer();
        buffer.write(new byte[]{GS, 0x76, 0x30, module, xL, xH, yL, yH});
        buffer.write(imageData);
        return buffer.toByteArray();
    }

    @Override
    public byte[] newLine() {
        return ESC_NEW_LINE;
    }

    @Override
    public byte[] cutPaper() {
        return ESC_CUTPAPER;
    }

    @Override
    public byte[] printerInfo() {
        return PRINTER_BRAND;
    }

    @Override
    public byte[] heartbeat() {
        return new byte[]{0x10, 0x04, 0x01};
    }
}
