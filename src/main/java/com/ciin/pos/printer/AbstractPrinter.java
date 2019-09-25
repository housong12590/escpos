package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPrinter implements Printer, Runnable {

    protected LinkedBlockingQueue<PrintTask> printTaskQueue;
    protected Device mDevice;
    protected boolean mBuzzer;
    protected OnPrintTaskCallback mPrintTaskCallback;
    protected OnPrinterErrorCallback mPrinterErrorCallback;
    protected long mLastActivationTime;
    protected boolean close;
    protected boolean blocking;
    private Thread mThread;
    private boolean done;

    AbstractPrinter(Device device) {
        this.mDevice = device;
        printTaskQueue = new LinkedBlockingQueue<>();
        mLastActivationTime = System.currentTimeMillis();
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

    @Override
    public void blocking(boolean blocking) {
        this.blocking = blocking;
    }

    @Override
    public void setPrintTaskCallback(OnPrintTaskCallback printTaskCallback) {
        this.mPrintTaskCallback = printTaskCallback;
    }

    @Override
    public void setPrinterErrorCallback(OnPrinterErrorCallback printerErrorCallback) {
        this.mPrinterErrorCallback = printerErrorCallback;
    }

    public OnPrintTaskCallback getPrintTaskCallback() {
        return mPrintTaskCallback;
    }

    public OnPrinterErrorCallback getPrinterErrorCallback() {
        return mPrinterErrorCallback;
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
                PrintTask printTask = printTaskQueue.poll(10, TimeUnit.SECONDS);
                if (printTask == null) {
                    if (!done) {
                        done = true;
                        printEnd();
                    }
                } else {
                    done = false;
                    if (printTask.isTimeOut()) {
                        LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", printTask.getTaskId()));
                        if (mPrintTaskCallback != null) {
                            mPrintTaskCallback.onError(this, printTask, "打印任务超时");
                        }
                    } else {
                        try {
                            if (print0(printTask)) {
                                LogUtils.debug(String.format("%s 打印成功.", printTask.getTaskId()));
                                if (mPrintTaskCallback != null) {
                                    mPrintTaskCallback.onSuccess(this, printTask);
                                }
                                // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                                Utils.sleep(printTask.getIntervalTime());
                            } else {
                                // 打印失败, 但是还要继续打印
                                printTaskQueue.offer(printTask);
                            }
                        } catch (Exception e) {
                            String errorMsg;
                            if (e instanceof TemplateParseException) {
                                errorMsg = "模版解析失败, 错误原因: " + e.getMessage();
                            } else {
                                errorMsg = "打印失败, 错误原因: " + e.getMessage();
                            }
                            if (mPrintTaskCallback != null) {
                                LogUtils.error(printTask.getTaskId() + " " + errorMsg);
                                mPrintTaskCallback.onError(this, printTask, errorMsg);
                            }
                        }
                    }

                }
            } catch (InterruptedException ignored) {

            }
        }
    }

    protected void printEnd() {

    }

    protected abstract boolean print0(PrintTask printTask) throws Exception;
}
