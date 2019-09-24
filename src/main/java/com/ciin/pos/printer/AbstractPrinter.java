package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractPrinter implements Printer, Runnable {

    protected Queue<PrintTask> printTaskQueue;
    protected Device mDevice;
    protected boolean mBuzzer;
    protected OnPrintTaskCallback mPrintTaskCallback;
    protected OnPrinterErrorCallback mPrinterErrorCallback;
    protected long mLastActivationTime;
    protected boolean stop;
    private Thread mThread;


    AbstractPrinter(Device device) {
        this.mDevice = device;
        printTaskQueue = new ConcurrentLinkedQueue<>();
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
        if (stop) {
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
    public void release() {
        this.stop = true;
        this.mThread = null;
    }

    @Override
    public void run() {
        while (!stop) {
            if (!printTaskQueue.isEmpty()) {
                PrintTask printTask = printTaskQueue.peek();
                if (printTask != null) {
                    // 打印任务超时, 丢弃掉任务
                    if (printTask.isTimeOut()) {
                        printTaskQueue.poll();
                        LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", printTask.getTaskId()));
                        if (mPrintTaskCallback != null) {
                            mPrintTaskCallback.onError(this, printTask, "打印任务超时");
                        }
                        continue;
                    }
                    try {
                        if (print0(printTask)) {
                            // 打印成功, 从任务队列里面移除
                            printTaskQueue.poll();
                            mLastActivationTime = System.currentTimeMillis();
                            if (mPrintTaskCallback != null) {
                                LogUtils.error(String.format("%s 打印成功.", printTask.getTaskId()));
                                mPrintTaskCallback.onSuccess(this, printTask);
                            }
                            // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                            Utils.sleep(printTask.getIntervalTime());
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
            } else if (System.currentTimeMillis() - mLastActivationTime > 10000) {
                heartbeat();
            } else {
                Utils.sleep(200);
            }
        }
    }

    protected abstract boolean print0(PrintTask printTask) throws TemplateParseException;

    protected void heartbeat(){

    }
}
