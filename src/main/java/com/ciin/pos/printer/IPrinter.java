package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.listener.OnPrinterListener;

public interface IPrinter {

    /**
     * 获取打印机设备信息
     */
    Device getDevice();

    /**
     * 设置打印机名字
     */
    void setPrinterName(String printerName);

    /**
     * 获取打印机名字
     */
    String getPrinterName();

    /**
     * 获取打印纸宽度
     */
    int getPaperWidth();

    /**
     * 添加一个打印任务
     *
     * @param printTask 打印任务
     */
    void print(PrintTask printTask);

    /**
     * 测试打印
     */
    void testPrint();

    /**
     * 设置打印机蜂蜜声
     */
    void buzzer(boolean buzzer);

    /**
     * 是否有蜂鸣声
     */
    boolean isBuzzer();

    /**
     * 设置打印机错误回调
     */
    void addPrinterListener(OnPrinterListener printerListener);

    /**
     * 移除打印机错误回调
     */
    void removePrinterListener(OnPrinterListener printerListener);

    /**
     * 打印机是否可用
     */
    boolean available();

    /**
     * 关闭打印机
     */
    void close();
}
