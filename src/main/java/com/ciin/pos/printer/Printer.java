package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.listener.OnPrinterListener;

import java.util.List;

public interface Printer<T extends Printer> {

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
     * 获取打印机所有任务列表
     */
    List<PrintTask> getPrintTasks();

    /**
     * 从打印队列中取消一个打印任务
     *
     * @param taskId 任务ID
     */
    void cancel(String taskId);

    /**
     * 清空打印任务列表
     */
    void clear();

    /**
     * 添加一个打印任务
     *
     * @param printTask 打印任务
     */
    void print(PrintTask printTask);

    /**
     * 添加到打印队列的最前端
     *
     * @param printTask 打印任务
     * @param first     为true时添加到队列的最前端,为false时则在最后
     */
    void print(PrintTask printTask, boolean first);

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

    void removePrinterListener(OnPrinterListener printerListener);

    int getIntervalTime();

    /**
     * 设置打印时间间隔
     */
    void setIntervalTime(int intervalTime);

    /**
     * 初始化打印列表
     */
    void initPrintTasks(List<PrintTask> printTasks);

    /**
     * 备用打印机
     */
    void setBackupPrinter(Printer printer);

    /**
     * 打印机是否可用
     */
    boolean available();

    /**
     * 关闭打印机
     */
    void close();
}
