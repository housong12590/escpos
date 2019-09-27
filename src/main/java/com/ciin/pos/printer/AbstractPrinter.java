package com.ciin.pos.printer;

import com.ciin.pos.listener.OnPrintTaskListener;
import com.ciin.pos.listener.OnPrinterListener;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPrinter implements Printer, Runnable {

    private static final int DEFAULT_WAIT_TIME = 30;
    private LinkedBlockingQueue<PrintTask> printTaskQueue;
    private Device mDevice;
    private boolean mBuzzer;
    private OnPrinterListener mPrinterListener;
    private boolean close;
    private Thread mThread;
    private boolean done;
    private int waitTime;

    AbstractPrinter(Device device) {
        this.mDevice = device;
        waitTime = getWaitTime();
        printTaskQueue = new LinkedBlockingQueue<>();
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public Device getDevice() {
        return mDevice;
    }

    @Override
    public List<PrintTask> getPrintTasks() {
        return new ArrayList<>(printTaskQueue);
    }

    @Override
    public void cancel(String taskId) {
        printTaskQueue.removeIf(printTask -> taskId.equals(printTask.getTaskId()));
    }

    @Override
    public void clear() {
        printTaskQueue.clear();
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
        printTaskQueue.offer(printTask);
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
    public void setPrinterListener(OnPrinterListener printerListener) {
        this.mPrinterListener = printerListener;
    }


    public OnPrinterListener getPrinterListener() {
        return mPrinterListener;
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
                PrintTask printTask = printTaskQueue.poll(waitTime, TimeUnit.SECONDS);
                if (printTask == null) {
                    if (!done) {
                        done = true;
                        printEnd();
                    }
                } else {
                    OnPrintTaskListener printTaskListener = printTask.getPrintTaskListener();
                    done = false;
                    if (printTask.isTimeout()) {
                        // 任务超时
                        printTaskTimeout(printTask);
                        continue;
                    }
                    try {
                        if (print0(printTask)) {
                            LogUtils.debug(String.format("%s 打印成功.", printTask.getTaskId()));
                            if (printTaskListener != null) {
                                printTaskListener.onSuccess(this, printTask);
                            }
                            // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                            Utils.sleep(printTask.getIntervalTime());
                        } else {
                            printTaskQueue.offer(printTask);
                        }
                    } catch (Exception e) {
                        if (e instanceof TimeoutException) {
                            printTaskTimeout(printTask);
                            continue;
                        }
                        String errorMsg;
                        if (e instanceof TemplateParseException) {
                            errorMsg = "模版解析失败: " + e.getMessage();
                        } else {
                            errorMsg = "打印失败: " + e.getMessage();
                        }
                        if (printTaskListener != null) {
                            LogUtils.error(printTask.getTaskId() + " " + errorMsg);
                            printTaskListener.onError(this, printTask, errorMsg);
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

    protected void printEnd() {

    }

    protected abstract boolean print0(PrintTask printTask) throws Exception;
}
