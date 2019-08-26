package com.cin.pos.printer;

import com.cin.pos.callback.OnCloseCallback;
import com.cin.pos.callback.OnConnectionErrorCallback;
import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.element.Document;
import com.cin.pos.parser.PrintTemplate;
import com.cin.pos.util.LoggerUtil;
import com.cin.pos.util.Util;

import java.io.File;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer {

    private Queue<PrintTaskRunnable> printTaskQueue;
    private Connection connection;
    private long lastPrintTime;
    private long connectKeepTime;
    private Device device;
    private Thread printerThread;
    private boolean bel;
    private boolean blocking;
    private int printerTimeOut;
    private OnPrintCallback mPrintCallback;
    private OnCloseCallback mCloseCallback;
    private OnConnectionErrorCallback mConnectionErrorCallback;

    public Printer(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
        printTaskQueue = new ConcurrentLinkedQueue<>();
    }

    public Device getDevice() {
        return device;
    }

    public int getPrintTaskSize() {
        return printTaskQueue.size();
    }

    public void cancelPrintTask() {
        printTaskQueue.clear();
    }

    public long getLastPrintTime() {
        return lastPrintTime;
    }

    public long getConnectKeepTime() {
        return connectKeepTime;
    }

    public void setConnectKeepTime(long millis) {
        this.connectKeepTime = millis;
    }

    public void setBel(boolean bel) {
        this.bel = bel;
    }

    public boolean isBel() {
        return bel;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking, int timeOut) {
        this.blocking = blocking;
        this.printerTimeOut = timeOut;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getPrinterTimeOut() {
        return printerTimeOut;
    }

    public void setPrinterTimeOut(int printerTimeOut) {
        this.printerTimeOut = printerTimeOut;
    }

    public String print(Document document) {
        String printId = generatePrintId();
        return (String) print(document, printId, 0);
    }

    public Object print(Document document, Object tag, int interval) {
        PrintTaskRunnable runnable = new PrintTaskRunnable(tag, this, interval);
        runnable.setDocument(document);
        addToPrintQueue(runnable, tag);
        return tag;
    }

    public String print(String templateContent, int interval) {
        String printId = generatePrintId();
        return (String) print(templateContent, null, printId, interval);
    }

    public String print(File templateFile, int interval) {
        String printId = generatePrintId();
        if (!templateFile.exists()) {
            LoggerUtil.error(templateFile.getAbsolutePath() + " not found...");
            return printId;
        }
        String readString = Util.readString(templateFile);
        return (String) print(readString, null, printId, interval);
    }

    public String print(String templateContent, Map data, int interval) {
        String printId = generatePrintId();
        return (String) print(templateContent, data, printId, interval);
    }

    public Object print(String templateContent, Map data, Object tag, int interval) {
        PrintTaskRunnable runnable = new PrintTaskRunnable(tag, this, interval);
        PrintTemplate printTemplate = new PrintTemplate();
        runnable.setTemplateParse(printTemplate, templateContent, data);
        addToPrintQueue(runnable, tag);
        return tag;
    }

    private synchronized void addToPrintQueue(PrintTaskRunnable runnable, Object tag) {
        printTaskQueue.add(runnable);
        LoggerUtil.debug(String.format("%s %s 添加到打印队列", this.connection, tag));
        refreshPrintTask();
    }


    private void refreshPrintTask() {
        if (printerThread == null) {
            LoggerUtil.debug(String.format("%s 创建新的打印线程", this.connection));
            printerThread = new Thread(new PrinterRunnable());
            printerThread.setName(this.connection.toString());
            printerThread.start();
        }
    }

    public void close() {
        printerThread.interrupt();
        printerThread = null;
        connection.close();
        if (mCloseCallback != null) {
            mCloseCallback.onClose(this);
        }
        LoggerUtil.debug(String.format("%s 打印机连接已销毁", this.connection));
    }

    public void setOnCloseCallback(OnCloseCallback callback) {
        this.mCloseCallback = callback;
    }

    public void setOnPrintCallback(OnPrintCallback printCallback) {
        this.mPrintCallback = printCallback;
    }

    OnPrintCallback getOnPrintCallback() {
        return this.mPrintCallback;
    }

    public void setOnConnectionErrorCallback(OnConnectionErrorCallback callback) {
        this.mConnectionErrorCallback = callback;
    }

    public OnConnectionErrorCallback getConnectionErrorCallback() {
        return mConnectionErrorCallback;
    }

    private String generatePrintId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    class PrinterRunnable implements Runnable {

        @Override
        public void run() {
            while (printerThread != null && !printerThread.isInterrupted()) {
                if (!printTaskQueue.isEmpty()) {
                    // 从队列获取第一个任务进行打印
                    PrintTaskRunnable runnable = printTaskQueue.poll();
                    if (runnable != null) {
                        // 执行打印任务
                        runnable.run();
                        // 记录最后一次打印的时间
                        lastPrintTime = System.currentTimeMillis();
                        // 兼容部分打印机, 如果性能差的,延迟一定的时间再发送打印指令
                        try {
                            Thread.sleep(runnable.getInterval());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (connectKeepTime > 0) {
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - lastPrintTime > connectKeepTime) {
                        close();
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                } else {
                    close();
                }
            }
        }
    }
}
