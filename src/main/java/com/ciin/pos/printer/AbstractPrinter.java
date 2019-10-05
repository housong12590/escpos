package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.listener.OnPrintTaskListener;
import com.ciin.pos.listener.OnPrinterListener;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPrinter implements Printer, Runnable {

    private static final int DEFAULT_WAIT_TIME = 30;
    private LinkedBlockingDeque<PrintTask> printTaskDeque;
    private OnPrinterListener mPrinterListener;
    private String printerName;
    private Device mDevice;
    private boolean mBuzzer;
    private boolean close;
    private Thread mThread;
    private boolean done;
    private int waitTime;
    private int intervalTime;
    private PrintTask curPrintTask;
    private Printer mBackupPrinter;
    private int printErrorCount;
    private final Object lock = new Object();
    private boolean mBackupPrinterResult;
    private boolean mEnableBackupPrinter;

    AbstractPrinter(Device device) {
        this.mDevice = device;
        waitTime = getWaitTime();
        printTaskDeque = new LinkedBlockingDeque<>();
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public Device getDevice() {
        return mDevice;
    }

    @Override
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    @Override
    public String getPrinterName() {
        return printerName;
    }

    @Override
    public List<PrintTask> getPrintTasks() {
        ArrayList<PrintTask> printTasks = new ArrayList<>(printTaskDeque);
        if (curPrintTask != null) {
            printTasks.add(0, curPrintTask);
        }
        return printTasks;
    }

    @Override
    public void initPrintTasks(List<PrintTask> printTasks) {
        printTaskDeque.addAll(printTasks);
    }

    @Override
    public void cancel(String taskId) {
        printTaskDeque.removeIf(printTask -> taskId.equals(printTask.getTaskId()));
    }

    @Override
    public void clear() {
        printTaskDeque.clear();
    }

    public int getWaitTime() {
        return DEFAULT_WAIT_TIME;
    }

    @Override
    public void print(PrintTask printTask) {
        if (close) {
            throw new RuntimeException("打印机已销毁, 无法执行新的打印任务");
        }
        printTask.setPrinter(this);
        printTaskDeque.offer(printTask);
        LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
    }

    @Override
    public void buzzer(boolean buzzer) {
        this.mBuzzer = buzzer;
    }

    @Override
    public boolean isBuzzer() {
        return this.mBuzzer;
    }

    public boolean isClose() {
        return close;
    }





    @Override
    public int getIntervalTime() {
        return intervalTime;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    @Override
    public void setPrinterListener(OnPrinterListener printerListener) {
        this.mPrinterListener = printerListener;
    }

    public OnPrinterListener getPrinterListener() {
        return mPrinterListener;
    }

    @Override
    public void setBackupPrinter(Printer printer) {
        this.mBackupPrinter = printer;
    }

    @Override
    public void close() {
        this.mThread.interrupt();
        this.close = true;
        this.mThread = null;
    }

    @Override
    public void run() {
        while (!close) {
            try {
                curPrintTask = printTaskDeque.poll(waitTime, TimeUnit.SECONDS);
                if (curPrintTask == null) {
                    if (!this.done) {
                        this.done = true;
                        printEnd();
                    } else {
                        if (mEnableBackupPrinter && this.available()) {
                            this.mEnableBackupPrinter = false;
                            this.done = false;
                            LogUtils.debug("关闭备用打印机: " + mBackupPrinter.getPrinterName());
                        }
                    }
                } else {
                    OnPrintTaskListener printTaskListener = curPrintTask.getPrintTaskListener();
                    done = false;
                    if (curPrintTask.isTimeout()) {
                        // 任务超时
                        printTaskTimeout(curPrintTask);
                        continue;
                    }
                    try {
                        if (!mEnableBackupPrinter) {
                            if (this.available() && print0(curPrintTask)) {
                                printErrorCount = 0;
                                LogUtils.debug(String.format("%s 打印成功.", curPrintTask.getTaskId()));
                                if (printTaskListener != null) {
                                    printTaskListener.onSuccess(this, curPrintTask);
                                }
                                // 打印完成之后,把当前的任务置空
                                curPrintTask = null;
                                // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                                if (intervalTime > 0) {
                                    Utils.sleep(intervalTime);
                                }
                            } else {
                                printErrorCount++;
                                if (mPrinterListener != null) {
                                    mPrinterListener.onPrinterError(this, new IOException("打印机连接失败"));
                                }
                                // 打印任务失败超过3次 启用备用打印机
                                // 如果备用打印机可用,直接转到备用用打印机打印
                                // 如果备用打印机不可用, 则添加到打印列表继续等待打印, 直到任务超时
                                if (printErrorCount >= 3 && this.mBackupPrinter != null && this.mBackupPrinter.available()) {
                                    mEnableBackupPrinter = true;
                                    LogUtils.debug("启用备用打印机: " + mBackupPrinter.getPrinterName());
                                }
                                printTaskDeque.addFirst(curPrintTask);
                                curPrintTask = null;
                            }
                        } else {
                            // 如果备用打印机也打印失败, 则继续添加到打印队列中
                            if (!this.useBackupPrinter(curPrintTask)) {
                                printTaskDeque.addFirst(curPrintTask);
                            }
                        }
                    } catch (Exception e) {
                        if (curPrintTask != null) {
                            if (e instanceof TimeoutException) {
                                printTaskTimeout(curPrintTask);
                                continue;
                            }
                            String errorMsg;
                            if (e instanceof TemplateParseException) {
                                errorMsg = "模版解析失败: " + e.getMessage();
                            } else {
                                errorMsg = "打印失败: " + e.getMessage();
                            }
                            if (printTaskListener != null) {
                                LogUtils.error(curPrintTask.getTaskId() + " " + errorMsg);
                                printTaskListener.onError(this, curPrintTask, errorMsg);
                            }
                        }
                    }
                }
            } catch (InterruptedException ignored) {

            }
        }
    }

    private void printTaskTimeout(PrintTask printTask) {
        LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", printTask.getTaskId()));
        if (printTask.getPrintTaskListener() != null) {
            printTask.getPrintTaskListener().onError(this, printTask, "打印任务超时");
        }
    }

    private boolean useBackupPrinter(PrintTask printTask) {
        LogUtils.debug(printTask.getTaskId() + String.format(" %s 转到 -> %s", this.toString(), mBackupPrinter.toString()));
        OnPrintTaskListener oldListener = printTask.getPrintTaskListener();
        printTask.setPrintTaskListener(new OnPrintTaskListener() {
            @Override
            public void onSuccess(Printer printer, PrintTask printTask) {
                mBackupPrinterResult = true;
                synchronized (lock) {
                    lock.notifyAll();
                }
                oldListener.onSuccess(printer, printTask);
            }

            @Override
            public void onError(Printer printer, PrintTask printTask, String errorMsg) {
                mBackupPrinterResult = false;
                synchronized (lock) {
                    lock.notifyAll();
                }
                oldListener.onError(printer, printTask, errorMsg);
            }
        });
        mBackupPrinter.print(printTask);
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mBackupPrinterResult;
    }


    protected void printEnd() {

    }

    protected abstract boolean print0(PrintTask printTask) throws Exception;

    @Override
    public String toString() {
        return this.getPrinterName();
    }
}
