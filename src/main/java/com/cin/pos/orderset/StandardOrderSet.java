package com.cin.pos.orderset;


import com.cin.pos.util.ByteBuffer;

public class StandardOrderSet implements OrderSet {

    private static final byte LF = 0x0A;
    private static final byte ESC = 0x1B;
    private static final byte GS = 0x1D;
    private static final byte FS = 0x1C;

    /*打印完成之蜂鸣声*/
    private static final byte[] BEL = new byte[]{ESC, 'B', 2, 1};
    /* 初始化打印机 ECS @ */
    private static final byte[] ESC_RESET = new byte[]{ESC, '@'};
    /* 靠左打印命令 */
    private static final byte[] ESC_ALIGN_LEFT = new byte[]{ESC, 'a', 0x00};
    /* 居中打印命令 */
    private static final byte[] ESC_ALIGN_CENTER = new byte[]{ESC, 'a', 0x01};
    /* 靠右打印命令 */
    private static final byte[] ESC_ALIGN_RIGHT = new byte[]{ESC, 'a', 0x02};
    /* 取消字体加粗 */
    private static final byte[] ESC_CANCEL_BOLD = new byte[]{ESC, 0x45, 0};
    /* 设置字体加粗 */
    private static final byte[] ESC_SET_BOLD = new byte[]{ESC, 0x45, 0x01};
    /* 取消字体下划线 */
    private static final byte[] FS_CANCEL_UNDERLINE = new byte[]{FS, 0x2D, 0x00};
    /* 设置字体下划线 */
    private static final byte[] FS_SET_UNDERLINE = new byte[]{FS, 0x2D, 0x01};
    /* 正常字体大小 */
    private static final byte[] ESC_FONT_SIZE_MIN = new byte[]{GS, '!', 0x00};
    /* 较大字体 */
    private static final byte[] ESC_FONT_SIZE_MID = new byte[]{GS, '!', 0x11};
    /* 超大字体 */
    private static final byte[] ESC_FONT_SIZE_MAX = new byte[]{GS, '!', 0x22};
    /* 打印并换行 */
    private static final byte[] ESC_NEW_LINE = new byte[]{LF};
    /* 切纸 */
    private static final byte[] ESC_CUTPAPER = new byte[]{ESC, 0x6D};

    /* 查询打印机状态 */
    private static byte[] ESC_STATUS(int n) {
        return new byte[]{ESC, 0x04, (byte) n};
    }

    @Override
    public byte[] reset() {
        return ESC_RESET;
    }

    @Override
    public byte[] printEnd() {
        return BEL;
    }

    @Override
    public byte[] status(int n) {
        return ESC_STATUS(n);
    }

    @Override
    public byte[] alignLeft() {
        return ESC_ALIGN_LEFT;
    }

    @Override
    public byte[] alignRight() {
        return ESC_ALIGN_RIGHT;
    }

    @Override
    public byte[] alignCenter() {
        return ESC_ALIGN_CENTER;
    }

    @Override
    public byte[] emphasize() {
        return ESC_SET_BOLD;
    }

    @Override
    public byte[] cancelEmphasize() {
        return ESC_CANCEL_BOLD;
    }

    @Override
    public byte[] underline() {
        return FS_SET_UNDERLINE;
    }

    @Override
    public byte[] cancelUnderline() {
        return FS_CANCEL_UNDERLINE;
    }

    @Override
    public byte[] textSizeX1() {
        return ESC_FONT_SIZE_MIN;
    }

    @Override
    public byte[] textSizeX2() {
        return ESC_FONT_SIZE_MID;
    }

    @Override
    public byte[] textSizeX3() {
        return ESC_FONT_SIZE_MAX;
    }

    @Override
    public byte[] testSize(int w, int h) {
        byte size = (byte) ((w == 2 ? 0x10 : w == 3 ? 0x20 : 0x00) | (h == 2 ? 0x01 : h == 3 ? 0x02 : 0x00));
        return new byte[]{GS, '!', size};
    }

    @Override
    public byte[] paperFeed(int n) {
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        return new byte[]{ESC, 'd', (byte) n};
    }

    @Override
    public byte[] printImage(byte module, byte xL, byte xH, byte yL, byte yH, byte[] imageData) {
        ByteBuffer buffer = new ByteBuffer();
        buffer.write(new byte[]{GS, 'v', 0x30, module, xL, xH, yL, yH});
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
}
