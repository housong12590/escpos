package com.cin.pos.printer;

import com.cin.pos.callback.OnPrintCallback1;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.exception.ConnectionException;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer1 implements Runnable {

    private Queue<PrintTask> printTaskQueue = new ConcurrentLinkedQueue<>();
    private Connection connection;
    private Device device;
    private Thread printerThread;
    private boolean buzzer;
    private long printLastTime;
    private long printerTimeOut = 60 * 1000;
    private final Object lock = new Object();
    private OnPrintCallback1 mPrintCallback;

    public Printer1(Connection connection, Device device) {
        this.connection = connection;
        this.device = device;
        // 开启打印机线程
        this.printerThread = new Thread(this);
        this.printerThread.setName(this.connection.toString());
        this.printerThread.start();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<PrintTask> getPrintTasks() {
        return new ArrayList<>(printTaskQueue);
    }

    public void cancel(String taskId) {
        synchronized (lock) {
            Iterator<PrintTask> it = printTaskQueue.iterator();
            while (it.hasNext()) {
                PrintTask printTask = it.next();
                if (taskId.equals(printTask.getTaskId())) {
                    if (mPrintCallback != null) {
                        mPrintCallback.onCancel(this, printTask);
                    }
                    it.remove();
                }
            }
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

    public long getPrinterTimeOut() {
        return printerTimeOut;
    }

    public void setPrinterTimeOut(long printerTimeOut) {
        this.printerTimeOut = printerTimeOut;
    }

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public void setPrintCallback(OnPrintCallback1 printCallback) {
        this.mPrintCallback = printCallback;
    }

    public void write(byte[] bytes) throws ConnectionException {
        this.connection.write(bytes);
        this.connection.flush();
    }

    public void flush() throws ConnectionException {
        this.connection.flush();
    }

    public int read(byte[] bytes) throws ConnectionException {
        return connection.read(bytes);
    }

    public void print(PrintTask printTask) {
        printTask.setPrinter(this);
        synchronized (lock) {
            printTaskQueue.offer(printTask);
            LoggerUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        }
    }

    @Override
    public void run() {
        while (printerThread != null && !printerThread.isInterrupted()) {
            if (!printTaskQueue.isEmpty()) {
                PrintTask printTask = printTaskQueue.peek();
                if (printTask != null) {
                    String taskId = printTask.getTaskId();
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - printTask.getCreateTime() > printerTimeOut) {
                        printTaskQueue.poll();
                        LoggerUtils.debug("%s 打印任务超时, 已取消本次打印");
                        if (mPrintCallback != null) {
                            mPrintCallback.onError(Printer1.this, printTask, "打印任务超时");
                        }
                        continue;
                    }
                    try {
                        printTask.call();
                        printLastTime = System.currentTimeMillis();
                        printTaskQueue.poll();
                        LoggerUtils.debug(String.format("%s 打印完成", taskId));
                        if (mPrintCallback != null) {
                            mPrintCallback.onSuccess(Printer1.this, printTask);
                        }
                    } catch (ConnectionException e) {
                        retryConnection();
                        LoggerUtils.error(String.format("%s 打印机连接失败, 正在尝试重连", taskId));
                    } catch (Exception e) {
                        String errorMsg = String.format("打印失败, 错误原因: %s", e.getMessage());
                        LoggerUtils.error(taskId + " " + errorMsg);
                        if (mPrintCallback != null) {
                            mPrintCallback.onError(Printer1.this, printTask, errorMsg);
                        }
                    }
                }
            } else if (System.currentTimeMillis() - printLastTime > 5000) {
                try {
                    write(device.getOrderSet().printerInfo());
                } catch (ConnectionException e) {
                    retryConnection();
                }
            }
        }
    }

    private void retryConnection() {
        while (!connection.isConnect()) {
            try {
                this.connection.doConnect();
                Utils.sleep(5000);
            } catch (ConnectionException ignored) {
            }
        }
    }

    public void destroy() {
        printerThread.interrupt();
    }
}
