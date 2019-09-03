package com.cin.pos.printer;

import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.exception.ConnectionException;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.util.LogUtils;
import com.cin.pos.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer implements Runnable {

    private Queue<PrintTask> printTaskQueue = new ConcurrentLinkedQueue<>();
    private Connection connection;
    private Device device;
    private Thread printerThread;
    private boolean buzzer;
    private long activeTime;
    private OnPrintCallback mPrintCallback;
    private boolean isStop;

    private final Object lock = new Object();

    public Printer(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
        // 开启打印机线程
        this.printerThread = new Thread(this);
        this.printerThread.setName(this.connection.toString());
        this.printerThread.start();
    }

    public List<PrintTask> getPrintTasks() {
        return new ArrayList<>(printTaskQueue);
    }

    public void cancel(String taskId) {
        synchronized (lock) {
            printTaskQueue.removeIf(printTask -> taskId.equals(printTask.getTaskId()));
        }
    }

    public void clear() {
        synchronized (lock) {
            printTaskQueue.clear();
        }
    }

    public Device getDevice() {
        return this.device;
    }

    public boolean isBuzzer() {
        return buzzer;
    }

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public void setPrintCallback(OnPrintCallback printCallback) {
        this.mPrintCallback = printCallback;
    }

    public void write(byte[] bytes) throws ConnectionException {
        this.connection.write(bytes);
    }

    public void writeAndFlush(byte[] bytes) throws ConnectionException {
        this.connection.write(bytes);
        this.connection.flush();
    }

    public void flush() throws ConnectionException {
        this.connection.flush();
    }

    public int read(byte[] bytes) throws ConnectionException {
        return connection.read(bytes);
    }

    private void printStatus() throws ConnectionException {
        OrderSet orderSet = device.getOrderSet();
        writeAndFlush(orderSet.status());
        byte[] bytes = new byte[50];
        read(bytes);
    }

    public void print(PrintTask printTask) {
        printTask.setPrinter(this);
        synchronized (lock) {
            printTaskQueue.offer(printTask);
            LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        }
    }


    @Override
    public void run() {
        while (printerThread != null && !isStop) {
            if (connection.isConnect()) {
                if (!printTaskQueue.isEmpty()) {
                    PrintTask printTask = printTaskQueue.peek();
                    if (printTask != null) {
                        String taskId = printTask.getTaskId();
                        if (printTask.isTimeOut()) {
                            printTaskQueue.poll();
                            LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", taskId));
                            if (mPrintCallback != null) {
                                mPrintCallback.onError(Printer.this, printTask, "打印任务超时");
                            }
                        } else {
                            try {
                                printStatus();
                                printTask.call();
                                activeTime = System.currentTimeMillis();
                                printTaskQueue.poll();
                                LogUtils.debug(String.format("%s 打印完成", taskId));
                                if (mPrintCallback != null) {
                                    mPrintCallback.onSuccess(Printer.this, printTask);
                                }
                                // 打印间隔时间
                                Utils.sleep(printTask.getIntervalTime());
                            } catch (ConnectionException e) {
                                retryConnection();
                            } catch (Exception e) {
                                String errorMsg = String.format("打印失败, 错误原因: %s", e.getMessage());
                                LogUtils.error(taskId + " " + errorMsg);
                                if (mPrintCallback != null) {
                                    mPrintCallback.onError(Printer.this, printTask, errorMsg);
                                }
                            }
                        }
                    }
                } else if (System.currentTimeMillis() - activeTime > 10000) {
                    activeTime = System.currentTimeMillis();
                    try {
                        writeAndFlush(device.getOrderSet().heartbeat());
                    } catch (ConnectionException e) {
                        retryConnection();
                    }
                } else {
                    Utils.sleep(200);
                }
            } else {
                LogUtils.debug("开始连接打印机....");
                retryConnection();
            }
        }
    }


    private void retryConnection() {
        while (!connection.isConnect() && !isStop) {
            try {
                try {
                    this.connection.doConnect();
                } catch (ConnectionException e) {
                    if (mPrintCallback != null) {
                        LogUtils.error("连接异常, 正在尝试重连  " + e.getMessage());
                        mPrintCallback.onConnectError(this);
                    }
                    throw e;
                }
                try {
                    printStatus();
                } catch (ConnectionException e) {
                    if (mPrintCallback != null) {
                        LogUtils.error("打印机异常, 请重启打印机  " + e.getMessage());
                        mPrintCallback.onPrinterError(this);
                    }
                    throw e;
                }
                LogUtils.debug(" 连接成功");
            } catch (Exception ignored) {
                Utils.sleep(5000);
            }
        }
    }

    public void release() {
        this.isStop = true;
        this.connection.close();
        this.printerThread.interrupt();
    }
}
