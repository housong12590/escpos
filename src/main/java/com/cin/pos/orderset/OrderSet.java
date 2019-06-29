package com.cin.pos.orderset;

/**
 * 打印指令集
 */
public interface OrderSet {

    /**
     * 初始化
     */
    byte[] reset();

    /**
     * 打印结束
     */
    byte[] printEnd();

    /**
     * 查询状态
     */
    byte[] status(int n);

    /**
     * 左对齐
     */
    byte[] alignLeft();

    /**
     * 右对齐
     */
    byte[] alignRight();

    /**
     * 居中对齐
     */
    byte[] alignCenter();

    /**
     * 文字加粗
     */
    byte[] emphasize();

    /**
     * 取消加粗
     */
    byte[] cancelEmphasize();

    /**
     * 添加下划线
     */
    byte[] underline();

    /**
     * 取消下划线
     */
    byte[] cancelUnderline();

    /**
     * 正常字体大小
     */
    byte[] textSizeX1();

    /**
     * 大号字体
     */
    byte[] textSizeX2();

    /**
     * 超大字体
     */
    byte[] textSizeX3();

    /**
     * 走纸
     *
     * @param n 行数
     */
    byte[] paperFeed(int n);

    /**
     * 打印光栅位图
     *
     * @param module
     * @param xL
     * @param xH
     * @param yL
     * @param yH
     * @param imageData 图片数据
     */
    byte[] printImage(byte module, byte xL, byte xH, byte yL, byte yH, byte[] imageData);

    /**
     * 换行
     */
    byte[] newLine();

    /**
     * 剪纸
     */
    byte[] cutPaper();

}
