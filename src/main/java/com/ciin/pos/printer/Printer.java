package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.exception.ConnectionException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;
import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.device.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer implements Runnable {

    private Queue<PrintTask> printTaskQueue = new ConcurrentLinkedQueue<>();
    private Connection connection;
    private Device device;
    private Thread mPrinterThread;
    private boolean buzzer;
    private long mLastActivationTime;
    private OnPrintTaskCallback mPrintTaskCallback;
    private OnPrinterErrorCallback mPrinterErrorCallback;
    private boolean isStop;
    private boolean mPrinterThreadAlive;

    private final Object lock = new Object();

    public Printer(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
    }

    private void startPrintThread() {
        // 初始化打印机线程
        if (this.mPrinterThread == null) {
            this.mPrinterThread = new Thread(this);
            this.mPrinterThread.setName(this.connection.toString());
        }
        if (!this.mPrinterThread.isAlive()) {
            mPrinterThreadAlive = true;
            this.mPrinterThread.start();
        }
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

    public void setPrintTaskCallback(OnPrintTaskCallback printCallback) {
        this.mPrintTaskCallback = printCallback;
    }

    public void setPrinterErrorCallback(OnPrinterErrorCallback printerErrorCallback) {
        this.mPrinterErrorCallback = printerErrorCallback;
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
        int readLength = read(bytes);
    }

    public void print(PrintTask printTask) {
        if (!this.mPrinterThreadAlive) {
            startPrintThread();
        }
        printTask.setPrinter(this);
        synchronized (lock) {
            printTaskQueue.offer(printTask);
            LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        }
    }


    private void execPrintTask() {
        PrintTask printTask = printTaskQueue.peek();
        if (printTask == null) {
            return;
        }
        String taskId = printTask.getTaskId();
        if (printTask.isTimeOut()) { // 判断任务是否超时
            // 打印任务已超时 , 不再执行打印了
            printTaskQueue.poll();
            LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", taskId));
            if (mPrintTaskCallback != null) {
                // 打印超时回调
                mPrintTaskCallback.onError(Printer.this, printTask, "打印任务超时");
                return;
            }
            try {
                // 检测打印机状态
                printStatus();
                // 执行打印任务
                printTask.call();
                // 从打印列表里移除
                printTaskQueue.poll();
                // 刷新最后激活时间
                mLastActivationTime = System.currentTimeMillis();
                LogUtils.debug(String.format("%s 打印完成", taskId));
                if (mPrintTaskCallback != null) {
                    // 打印完成回调
                    mPrintTaskCallback.onSuccess(Printer.this, printTask);
                }
            } catch (Exception e) {
                if (e instanceof ConnectionException) {
                    // 连接异常, 执行重新连接
                    retryConnection();
                } else {
                    // 打印抛出现不可逆异常, 直接返回给调用方
                    String errorMsg = String.format("打印失败, 错误原因: %s", e.getMessage());
                    LogUtils.error(taskId + " " + errorMsg);
                    if (mPrintTaskCallback != null) {
                        // 打印错误回调
                        mPrintTaskCallback.onError(Printer.this, printTask, errorMsg);
                    }
                }
            }
            // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
            Utils.sleep(printTask.getIntervalTime());
        }
    }


    @Override
    public void run() {
        while (!isStop) {
            if (!connection.isConnect()) {
                retryConnection();
            } else if (!printTaskQueue.isEmpty()) {
                execPrintTask();
            } else if (System.currentTimeMillis() - mLastActivationTime > 10000) {
                mLastActivationTime = System.currentTimeMillis();
                try {
                    writeAndFlush(device.getOrderSet().heartbeat());
                } catch (ConnectionException e) {
                    retryConnection();
                }
            } else {
                Utils.sleep(200);
            }
        }
    }


    private void retryConnection() {
        while (!connection.isConnect() && !isStop) {
            try {
                try {
                    // 建立打印机连接
                    this.connection.doConnect();
                } catch (ConnectionException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("连接异常, 正在尝试重连  " + e.getMessage());
                        mPrinterErrorCallback.onConnectError(this, connection);
                    }
                    throw e;
                }
                try {
                    // 检测打印机是否可用
                    printStatus();
                } catch (ConnectionException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("打印机异常, 请重启打印机  " + e.getMessage());
                        mPrinterErrorCallback.onPrinterError(this);

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
    }
}
