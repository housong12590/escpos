package com.ciin.pos.printer;

import com.ciin.pos.listener.OnPrinterListener;
import com.ciin.pos.device.Device;

import java.util.List;

public interface Printer {

    /**
     * 获取打印机设备信息
     */
    Device getDevice();

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
    void setPrinterListener(OnPrinterListener printerListener);

    /**
     * 关闭打印机
     */
    void close();
}
