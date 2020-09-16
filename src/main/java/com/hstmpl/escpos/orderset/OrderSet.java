package com.hstmpl.escpos.orderset;

import com.hstmpl.escpos.enums.Align;
import com.hstmpl.escpos.enums.Size;

/**
 * 打印指令集
 *
 * @author hous
 */
public interface OrderSet {

    /**
     * 初始化
     */
    byte[] reset();

    /**
     * 打印结束, 打印机的蜂鸣声
     */
    byte[] buzzer(int count);

    /**
     * 换行
     */
    byte[] newLine();

    /**
     * 剪纸
     */
    byte[] cutPaper();

    /**
     * 打印机状态,等待打印缓存区打印完成后才处理此命令
     */
    byte[] status();

    /**
     * 文字对齐方式
     */
    byte[] align(Align align);

    /**
     * 文字是否加粗
     */
    byte[] bold(boolean bold);

    /**
     * 添加下划线
     */
    byte[] underline(boolean underline);

    /**
     * 文字字体大小
     */
    byte[] textSize(Size size);

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
     * 打印机信息
     */
    byte[] printerInfo();

    /**
     * 打印机连接心跳
     */
    byte[] heartbeat();

}
