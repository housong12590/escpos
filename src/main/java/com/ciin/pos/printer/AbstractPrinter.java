package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.listener.OnPaperChangeListener;
import com.ciin.pos.listener.OnPrintEventListener;
import com.ciin.pos.listener.OnPrinterListener;
import com.ciin.pos.listener.PrintEvent;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.ConvertUtils;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPrinter implements Printer, Runnable {

    private static final int DEFAULT_WAIT_TIME = 30;
    private LinkedBlockingDeque<PrintTask> printTaskDeque;
    private List<OnPrinterListener> mPrinterListeners;
    private String printerName;
    private Device mDevice;
    private boolean mBuzzer;
    private boolean mClose;
    private Thread mThread;
    private boolean done;
    private int waitTime;
    private long intervalTime;
    private PrintTask curPrintTask;
    private Printer mBackupPrinter;
    private int printErrorCount;
    private boolean mBackupPrinterResult;
    private boolean mEnableBackupPrinter;
    private boolean mEnabledKeepPrint;
    private boolean mPrintEnd;

    public AbstractPrinter(Device device) {
        this.mDevice = device;
        mPrinterListeners = new ArrayList<>();
        waitTime = getWaitTime();
        printTaskDeque = new LinkedBlockingDeque<>();
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
        for (PrintTask printTask : printTasks) {
            print(printTask);
        }
    }

    @Override
    public void cancel(String taskId) {
        Iterator<PrintTask> it = printTaskDeque.iterator();
        while (it.hasNext()) {
            PrintTask printTask = it.next();
            if (printTask.getTaskId().equals(taskId)) {
                printTask.getDefaultListener()
                        .onEventTriggered(this, printTask, PrintEvent.CANCEL, null);
                it.remove();
            }
        }
    }

    @Override
    public void testPrint() {
        PrintTask printTask = new PrintTask(new Template(Constants.TEST_TEMPLATE));
        printTask.setTempPrint(true);
        print(printTask);
    }

    @Override
    public void clear() {
        printTaskDeque.clear();
    }

    @Override
    public int getPaperWidth() {
        return mDevice.getPaperWidth();
    }

    public int getWaitTime() {
        return DEFAULT_WAIT_TIME;
    }

    @Override
    public void print(PrintTask printTask) {
        print(printTask, false);
    }

    @Override
    public void print(PrintTask printTask, boolean first) {
        if (mClose) {
            throw new RuntimeException("打印机已销毁, 无法执行新的打印任务");
        }
        printTask.setPrinter(this);
        if (first) {
            printTaskDeque.addFirst(printTask);
        } else {
            printTaskDeque.addLast(printTask);
        }
        LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        mPrintEnd = false;
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.start();
        }
    }

    @Override
    public PrintResult syncPrint(PrintTask printTask) {
        if (Thread.currentThread() == mThread) {
            throw new RuntimeException("不能在打印线程中调用同步方法");
        }
        PrintResult result = new PrintResult();
        CountDownLatch downLatch = new CountDownLatch(1);
        OnPrintEventListener listener = printTask.getPrintEventListener();
        if (listener == null) {
            printTask.setPrintEventListener(new OnPrintEventListener() {
                @Override
                public void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj) {
                    switch (event) {
                        case TIMEOUT:
                            result.setSuccess(false);
                            result.setMessage("打印超时");
                            break;
                        case ERROR:
                            result.setSuccess(false);
                            result.setMessage(ConvertUtils.toString(obj));
                            break;
                        case SUCCESS:
                            result.setSuccess(true);
                            result.setMessage("打印成功");
                            break;
                        case CANCEL:
                            result.setSuccess(false);
                            result.setMessage("取消打印");
                            break;
                    }
                    if (event != PrintEvent.PREPARE) {
                        downLatch.countDown();
                    }
                }
            });
        }
        print(printTask, true);
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            result.setSuccess(false);
            result.setMessage("方法调用超时");
        }
        return result;
    }

    @Override
    public PrintResult syncTestPrint() {
        PrintTask printTask = new PrintTask(new Template(Constants.TEST_TEMPLATE));
        printTask.setTempPrint(true);
        return syncPrint(printTask);
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
        return mClose;
    }

    @Override
    public long getIntervalTime() {
        return intervalTime;
    }

    @Override
    public void setIntervalTime(long millis) {
        this.intervalTime = millis;
    }

    @Override
    public void addPrinterListener(OnPrinterListener printerListener) {
        if (printerListener != null) {
            this.mPrinterListeners.add(printerListener);
        }
    }

    @Override
    public void removePrinterListener(OnPrinterListener printerListener) {
        if (printerListener != null) {
            this.mPrinterListeners.remove(printErrorCount);
        }
    }

    @Override
    public void setBackupPrinter(Printer printer) {
        this.mBackupPrinter = printer;
    }

    @Override
    public void close() {
        if (this.printTaskDeque != null) {
            this.printTaskDeque.clear();
        }
        if (!this.mClose) {
            printEnd();
        }
        this.mClose = true;
    }

    private void printEnd() {
        if (mThread != null) {
            this.mThread.interrupt();
            this.mThread = null;
        }
        this.mPrintEnd = true;
        printEnd0();
    }


    @Override
    public void setEnabledKeepPrint(boolean keepPrint) {
        this.mEnabledKeepPrint = keepPrint;
    }

    @Override
    public void run() {
        while (!mClose && !mPrintEnd) {
            try {
                curPrintTask = printTaskDeque.poll(waitTime, TimeUnit.SECONDS);
                // 如果是短暂的打印, 并且队列中没有任务了, 直接关闭
                if (curPrintTask == null) {
                    if (!mEnabledKeepPrint) {
                        printEnd();
                    } else if (!this.done) {
                        this.done = true;
                        printEnd0();
                    } else {
                        if (mEnableBackupPrinter && this.available()) {
                            this.mEnableBackupPrinter = false;
                            this.done = false;
                            LogUtils.debug("关闭备用打印机: " + mBackupPrinter.getPrinterName());
                        }
                    }
                } else {
                    done = false;
                    if (curPrintTask.isTimeout()) {
                        // 任务超时
                        printTaskTimeout(curPrintTask);
                        continue;
                    }
                    try {
                        if (!mEnableBackupPrinter || curPrintTask.isTempPrint()) {
                            curPrintTask.getDefaultListener().onEventTriggered(this, curPrintTask, PrintEvent.PREPARE, null);
                            if (this.available() && print0(curPrintTask)) {
                                printErrorCount = 0;
                                LogUtils.debug(String.format("%s 打印成功", curPrintTask.getTaskId()));
                                curPrintTask.getDefaultListener().onEventTriggered(this, curPrintTask, PrintEvent.SUCCESS, null);
                                // 打印完成之后,把当前的任务置空
                                curPrintTask = null;
                                // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                                if (intervalTime > 0) {
                                    Utils.sleep(intervalTime);
                                }
                            } else {
                                mPrinterListeners.forEach(listener -> listener.onPrinterError(AbstractPrinter.this, new IOException("打印机连接失败")));
                                // 临时打印,错误时不添加到当前打印机的打印列表中
                                if (curPrintTask.isTempPrint() || !mEnabledKeepPrint) {
                                    curPrintTask.getDefaultListener().onEventTriggered(this, curPrintTask, PrintEvent.ERROR, "连接错误");
                                    LogUtils.debug(String.format("%s 打印失败", curPrintTask.getTaskId()));
                                    curPrintTask = null;
                                } else {
                                    printErrorCount++;
                                    // 打印任务失败超过3次 启用备用打印机
                                    // 如果备用打印机可用,直接转到备用用打印机打印
                                    // 如果备用打印机不可用, 则添加到打印列表继续等待打印, 直到任务超时
                                    LogUtils.debug(String.format("%s 打印失败, 打印任务将重新添加到队列中, 等待继续打印", curPrintTask.getTaskId()));
                                    if (printErrorCount >= 3 && this.mBackupPrinter != null && this.mBackupPrinter.available()) {
                                        mEnableBackupPrinter = true;
                                        LogUtils.debug("启用备用打印机: " + mBackupPrinter.getPrinterName());
                                    }
                                    printTaskDeque.addFirst(curPrintTask);
                                    curPrintTask = null;
                                }
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
                            printTaskError(curPrintTask, errorMsg);
                        }
                    }
                }
            } catch (InterruptedException ignored) {

            }
        }
    }

    private void printTaskError(PrintTask printTask, String errorMsg) {
        LogUtils.error(printTask.getTaskId() + " " + errorMsg);
        printTask.getDefaultListener().onEventTriggered(this, printTask, PrintEvent.ERROR, errorMsg);
    }

    private void printTaskTimeout(PrintTask printTask) {
        LogUtils.debug(String.format("%s 打印任务超时, 已取消本次打印", printTask.getTaskId()));
        printTask.getDefaultListener().onEventTriggered(this, printTask, PrintEvent.TIMEOUT, null);
    }

    private boolean useBackupPrinter(PrintTask printTask) {
        boolean paperChange = false;
        OnPaperChangeListener paperChangeLister = printTask.getPaperChangeLister();
        if (paperChangeLister != null && getPaperWidth() != mBackupPrinter.getPaperWidth()) {
            paperChange = true;
            paperChangeLister.onChange(mBackupPrinter, printTask);
        }
        printTask.setTempPrint(true);
        LogUtils.debug(printTask.getTaskId() + String.format(" %s 转到 -> %s", this.toString(), mBackupPrinter.toString()));
        OnPrintEventListener oldListener = printTask.getPrintEventListener();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        boolean finalPaperChange = paperChange;
        printTask.setPrintEventListener(new OnPrintEventListener() {
            @Override
            public void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj) {
                mBackupPrinterResult = event == PrintEvent.SUCCESS;
                if (oldListener != null) {
                    oldListener.onEventTriggered(printer, printTask, event, obj);
                }
                if (!mBackupPrinterResult && finalPaperChange) {
                    paperChangeLister.onChange(AbstractPrinter.this, printTask);
                }
                countDownLatch.countDown();
            }
        });
        mBackupPrinter.print(printTask);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            mBackupPrinterResult = false;
        }
        printTask.setPrinter(this);
        printTask.setTempPrint(false);
        printTask.setPrintEventListener(oldListener);
        return mBackupPrinterResult;
    }

    protected abstract void printEnd0();

    protected abstract boolean print0(PrintTask printTask) throws Exception;

    @Override
    public String toString() {
        return this.getPrinterName();
    }
}
