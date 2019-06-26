package com.cin.pos.printer;

import com.cin.pos.callback.OnCloseCallback;
import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.element.Document;
import com.cin.pos.parser.TemplateParse;
import com.cin.pos.util.LoggerUtil;
import com.cin.pos.util.Util;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Printer {

    private static ExecutorService es = Executors.newCachedThreadPool();
    private LinkedList<PrintTaskRunnable> printTaskQueue = new LinkedList<>();
    private final Object obj = new Object();
    private Connection connection;
    private long lastPrintTime;
    private long connectKeepTime;
    private long invalidTime = 1800;
    private Device device;
    private boolean alwaysKeep = false;
    private Future<?> future;
    private OnPrintCallback mPrintCallback;
    private OnCloseCallback mCloseCallback;

    public Printer(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
    }

    public Device getDevice() {
        return device;
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

    public void alwaysKeepConnect(boolean alwaysKeep) {
        this.alwaysKeep = alwaysKeep;
    }

    public Connection getConnection() {
        return connection;
    }

    public String print(Document document, int interval) {
        String printId = generatePrintId();
        return (String) print(document, printId, interval, false);
    }

    public Object print(Document document, Object tag, int interval, boolean blocking) {
        PrintTaskRunnable runnable = new PrintTaskRunnable(tag, this, interval, blocking);
        runnable.setDocument(document);
        printTaskQueue.add(runnable);
        refreshTaskStatus();
        return tag;
    }

    public String print(String templateContent, int interval) {
        String printId = generatePrintId();
        return (String) print(templateContent, null, printId, interval, false);
    }

    public String print(File templateFile, int interval) {
        String printId = generatePrintId();
        if (!templateFile.exists()) {
            LoggerUtil.error(templateFile.getAbsolutePath() + " not found...");
            return printId;
        }
        String readString = Util.readString(templateFile);
        return (String) print(readString, null, printId, interval, false);
    }

    public String print(String templateContent, Map data, int interval, boolean blocking) {
        String printId = generatePrintId();
        return (String) print(templateContent, data, printId, interval, blocking);
    }

    public Object print(String templateContent, Map data, Object tag, int interval, boolean blocking) {
        PrintTaskRunnable runnable = new PrintTaskRunnable(tag, this, interval, blocking);
        TemplateParse templateParse = new TemplateParse();
        runnable.setTemplateParse(templateParse, templateContent, data);
        printTaskQueue.add(runnable);
        refreshTaskStatus();
        return tag;
    }


    private void refreshTaskStatus() {
        if (future == null || future.isDone()) {
            future = es.submit(new PrinterRunnable());
        }
        synchronized (obj) {
            obj.notifyAll();
        }
    }

    public void close() {
        future.cancel(true);
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

    private String generatePrintId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    class PrinterRunnable implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (!printTaskQueue.isEmpty()) {
                    PrintTaskRunnable runnable = printTaskQueue.removeFirst();
                    runnable.run();
                    lastPrintTime = System.currentTimeMillis();
                    // 兼容部分打印机, 如果性能差的,延迟一定的时间再发送打印指令
                    int interval = runnable.getInterval();
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (alwaysKeep) {
                    try {
                        synchronized (obj) {
                            LoggerUtil.debug(String.format("%s 无打印任务,进入等待状态...", connection));
                            obj.wait();
                            LoggerUtil.debug(String.format("%s 激活打印线程,马上开始打印...", connection));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (connectKeepTime > 0) {
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - lastPrintTime > connectKeepTime) {
//                        LoggerUtil.debug(String.format("线程已超过等待时间%sms,连接即将关闭", connectKeepTime));
                        close();
                    } else {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                        }
                    }
                } else {
                    close();
                }
            }
        }
    }
}
