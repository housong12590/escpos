package com.cin.pos.printer;

import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.exception.ConnectionException;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
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

    private final Object lock = new Object();
    private OnPrintCallback mPrintCallback;

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

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public void setPrintCallback(OnPrintCallback printCallback) {
        this.mPrintCallback = printCallback;
    }

    public void write(byte[] bytes) throws ConnectionException {
        this.connection.write(bytes);
        this.connection.flush();
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

    private void checkOnline() throws ConnectionException {
        OrderSet orderSet = device.getOrderSet();
        writeAndFlush(orderSet.status(1));
        byte[] bytes = new byte[50];
        int read = read(bytes);
        if (read != -1) {

        }
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
            try {
                if (!printTaskQueue.isEmpty()) {
                    PrintTask printTask = printTaskQueue.peek();
                    if (printTask != null) {
                        // 打印间隔时间
                        Utils.sleep(printTask.getIntervalTime());
                        String taskId = printTask.getTaskId();
                        if (!printTask.isTimeOut()) {
                            printTaskQueue.poll();
                            LoggerUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", taskId));
                            if (mPrintCallback != null) {
                                mPrintCallback.onError(Printer.this, printTask, "打印任务超时");
                            }
                            continue;
                        }
                        try {
                            checkOnline();
                            printTask.call();
                            activeTime = System.currentTimeMillis();
                            printTaskQueue.poll();
                            LoggerUtils.debug(String.format("%s 打印完成", taskId));
                            if (mPrintCallback != null) {
                                mPrintCallback.onSuccess(Printer.this, printTask);
                            }
                        } catch (Exception ex) {
                            if (ex instanceof ConnectionException) {
                                throw (ConnectionException) ex;
                            }
                            printTaskQueue.poll();
                            String errorMsg = String.format("打印失败, 错误原因: %s", ex.getMessage());
                            LoggerUtils.error(taskId + " " + errorMsg);
                            if (mPrintCallback != null) {
                                mPrintCallback.onError(Printer.this, printTask, errorMsg);
                            }
                        }
                    }
                } else if (System.currentTimeMillis() - activeTime > 10000) {
                    activeTime = System.currentTimeMillis();
                    writeAndFlush(device.getOrderSet().status(1));
                } else {
                    Utils.sleep(200);
                }
            } catch (ConnectionException e) {
                LoggerUtils.error("打印机连接失败, 正在尝试重连");
                retryConnection();
            }

        }
    }


    private void retryConnection() {
        while (!connection.isConnect()) {
            try {
                this.connection.doConnect();
                LoggerUtils.debug(" 连接成功");
            } catch (ConnectionException ignored) {
                if (mPrintCallback != null) {
                    mPrintCallback.onConnectError(this);
                }
                Utils.sleep(5000);
            }
        }
    }

    public void release() {
        this.connection.close();
        this.printerThread.interrupt();
    }
}
