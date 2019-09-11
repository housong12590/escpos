package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.ConnectionException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer1 implements Runnable {

    private Queue<PrintTask> printTaskQueue = new ConcurrentLinkedQueue<>();
    private Connection connection;
    private Device device;
    private Thread printerThread;
    private boolean buzzer;
    private long activeTime;
    private OnPrintTaskCallback mPrintTaskCallback;
    private OnPrinterErrorCallback mPrinterErrorCallback;
    private boolean isStop;
    private boolean usable;

    private final Object lock = new Object();

    public Printer1(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
        // 初始化打印机线程
        initPrinterThread();
    }

    private void initPrinterThread() {
        this.printerThread = new Thread(this);
        this.printerThread.setName(this.connection.toString());
    }

    private void startPrintThread() {
        if (!this.printerThread.isAlive()) {
            this.printerThread.start();
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
        if (!this.printerThread.isAlive()) {
            startPrintThread();
        }
        printTask.setPrinter(this);
        synchronized (lock) {
            printTaskQueue.offer(printTask);
            LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        }
    }


    @Override
    public void run() {
        while (!isStop) {

        }
    }


    private void retryConnection() {
        while (!connection.isConnect() && !isStop) {
            try {
                try {
                    this.connection.doConnect();
                } catch (ConnectionException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("连接异常, 正在尝试重连  " + e.getMessage());
                        mPrinterErrorCallback.onConnectError(this, connection);
                    }
                    throw e;
                }
                try {
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
        this.printerThread.interrupt();
    }
}