package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.device.Device;

import java.io.IOException;
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
     * 获取打印机连接
     */
    Connection getConnection();

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
     * 检测打印机是否连接
     */
    boolean checkConnect() throws IOException;

    /**
     * 是否有蜂鸣声
     */
    boolean isBuzzer();

    /**
     * 设置打印任务回调
     */
    void setPrintTaskCallback(OnPrintTaskCallback printTaskCallback);

    /**
     * 设置打印机错误回调
     */
    void setPrinterErrorCallback(OnPrinterErrorCallback printerErrorCallback);

    /**
     * 回收资源
     */
    void release();
}
