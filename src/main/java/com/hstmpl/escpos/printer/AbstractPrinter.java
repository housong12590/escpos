package com.hstmpl.escpos.printer;

import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.exception.PrintTimeoutException;
import com.hstmpl.escpos.parser.Document;
import com.hstmpl.escpos.parser.Template;
import com.hstmpl.escpos.util.ConvertUtils;
import com.hstmpl.escpos.util.LogUtils;
import com.hstmpl.escpos.Constants;
import com.hstmpl.escpos.listener.OnPaperChangeListener;
import com.hstmpl.escpos.listener.OnPrintEventListener;
import com.hstmpl.escpos.listener.OnPrinterListener;
import com.hstmpl.escpos.listener.PrintEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author hous
 */
public abstract class AbstractPrinter implements Printer, Runnable {

    private static final long PRINT_INTERVAL_TIME = 1000;
    private static final long ERROR_INTERVAL_TIME = 5000;
    private static final int DEFAULT_WAIT_TIME = 10;
    private LinkedBlockingDeque<PrintTask> printTaskDeque;
    private List<OnPrinterListener> mPrinterListeners;
    private String printerName;
    private Device mDevice;
    private boolean mBuzzer;
    private boolean mClose;
    private Thread mThread;
    private boolean mDone;
    private int waitTime;
    private long mPrintIntervalTime = PRINT_INTERVAL_TIME;
    private long mErrorIntervalTime = ERROR_INTERVAL_TIME;
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
                printTask.getDefaultListener().onEventTriggered(this, printTask, PrintEvent.CANCEL, null);
                it.remove();
            }
        }
    }

    @Override
    public void testPrint() {
        Template template = new Template(Constants.TEST_TEMPLATE);
        Document document = template.toDocument(null);
        PrintTask printTask = new PrintTask(document);
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
        // 打印任务绑定打印机
        printTask.setPrinter(this);
        // 添加到打印列表的头部
        if (first) {
            printTaskDeque.addFirst(printTask);
        } else {
            printTaskDeque.addLast(printTask);
        }
        LogUtils.debug(String.format("%s 添加到打印队列", printTask.getTaskId()));
        // 恢复打印完成标志位
        mPrintEnd = false;
        // 检测打印工作线程
        if (mThread == null) {
            mThread = new Thread(this);
            mThread.setName(this.getPrinterName());
            mThread.start();
        }
    }

    @Override
    public PrintResult syncPrint(PrintTask printTask) {
        // 检测一下调用线程和打印线程是否是同一个线程, 如果是同一个线程则会发生死锁
        if (Thread.currentThread() == mThread) {
            throw new RuntimeException("不能在打印线程中调用同步方法");
        }
        PrintResult result = new PrintResult();
        // 创建线程同步器
        CountDownLatch downLatch = new CountDownLatch(1);
        // 获取该任务的打印回调
        final OnPrintEventListener listener = printTask.getPrintEventListener();
        // 替换打印任务的回调
        printTask.setPrintEventListener(new OnPrintEventListener() {
            @Override
            public void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj) {
                if (listener != null) {
                    listener.onEventTriggered(printer, printTask, event, obj);
                }
                result.setMessage(event.getValue());
                switch (event) {
                    case ERROR:
                        result.setMessage(ConvertUtils.toString(obj));
                        break;
                    case SUCCESS:
                        result.setSuccess(true);
                        break;
                }
                downLatch.countDown();
                // 打印完成后, 把原先设置的打印回调设置回去
                printTask.setPrintEventListener(listener);
            }
        });
        // 同步打印任务因为要阻塞线程调用线程, 应该优先处理
        // 把打印任务添加到打印队列头部等待打印
        print(printTask, true);
        try {
            // 等待打印回调结果, 如果10秒还没有结果 ,则认为超时了 , 不再等待
            boolean await = downLatch.await(10, TimeUnit.SECONDS);
            if (!await) {
                // 任务已经超时, 取消该打印任务
                cancel(printTask.getTaskId());
                result.setMessage("method call timeout");
            }
        } catch (InterruptedException ignored) {
            result.setMessage("print thread interrupt");
        }
        return result;
    }

    @Override
    public PrintResult syncTestPrint() {
        Template template = new Template(Constants.TEST_TEMPLATE);
        Document document = template.toDocument(null);
        PrintTask printTask = new PrintTask(document);
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
    public long getPrintInterval() {
        return mPrintIntervalTime;
    }

    @Override
    public void setPrintInterval(long interval) {
        this.mPrintIntervalTime = interval;
    }

    @Override
    public long getErrorInterval() {
        return mErrorIntervalTime;
    }

    @Override
    public void setErrorInterval(long interval) {
        this.mErrorIntervalTime = interval;
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
        LogUtils.debug("关闭打印机");
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
                // 从打印队列中获取打印任务, 如果在@waitTime时间内还没有获取到结果,则会返回一个null对象
                curPrintTask = printTaskDeque.poll(mEnabledKeepPrint ? waitTime : 0, TimeUnit.SECONDS);
                // 打印队列中已经没有打印任务了
                if (curPrintTask == null) {
                    // 检测用户是否设置了保持打印, 如果没有设置保持打印, 则关闭当前的打印线程
                    if (!mEnabledKeepPrint) {
                        printEnd();
                    } else if (!this.mDone) { // 设置了保持打印, 但没有打印任务, 关闭打印持有的相关连接
                        this.mDone = true;
                        printEnd0();
                    } else {
                        // 如果当前是使用备用打印机进行打印, 先检测一下 ,当前的打印机是否可用
                        // 如果可用则关闭备用打印机模式, 使用当前打印机进行下次打印
                        if (mEnableBackupPrinter && this.available()) {
                            this.mEnableBackupPrinter = false;
                            this.mDone = false;
                            LogUtils.debug("关闭备用打印机: " + mBackupPrinter.getPrinterName());
                        }
                    }
                } else {
                    mDone = false;
                    // 检测打印任务是否超时
                    if (curPrintTask.isTimeout()) {
                        // 任务已经超时, 回调相关方法, 准备进行下次打印
                        printTaskTimeout(curPrintTask);
                        // 因为当前的任务已经超时, 所以不需要进行后续的打印操作
                        continue;
                    }
                    try {
                        // 检测当前是否开启了备用打印模式
                        // 如果备用打印模式, 则不能打印当前打印机的一些临时任务, 比如打印测试纸
                        if (!mEnableBackupPrinter || curPrintTask.isTempPrint()) {
                            // 检测当前打印机是否可用, 如果可用则发送打印指令
                            if (this.available() && print0(curPrintTask)) {
                                // 当前的打印任务已经打印完成
                                printErrorCount = 0;
                                LogUtils.debug(String.format("%s 打印成功", curPrintTask.getTaskId()));
                                // 回调打印完成的回调
                                curPrintTask.getDefaultListener().onEventTriggered(this, curPrintTask, PrintEvent.SUCCESS, null);
                                // 打印完成之后,把当前的任务置空
                                curPrintTask = null;
                                // 兼容部分性能差的打印机, 两次打印间需要间隔一定的时间
                                if (mPrintIntervalTime > 0) {
                                    Thread.sleep(mPrintIntervalTime);
                                }
                            } else {
                                // 如果当前打印机已经调用了close方法,则退出不执行后续操作
                                if (isClose()) {
                                    return;
                                }
                                // 可能由于打印机不可用, 或者发送打印数据出现异常
                                // 调用打印机错误回调
                                mPrinterListeners.forEach(listener -> listener.onPrinterError(AbstractPrinter.this,
                                        new IOException("printer connect error")));
                                // 临时打印,错误时不添加到当前打印机的打印列表中
                                if (curPrintTask.isTempPrint() || !mEnabledKeepPrint) {
                                    curPrintTask.getDefaultListener().onEventTriggered(this, curPrintTask, PrintEvent.ERROR, "connect error");
                                    LogUtils.debug(String.format("%s 打印失败", curPrintTask.getTaskId()));
                                } else {
                                    printErrorCount++;
                                    // 打印任务失败超过3次 启用备用打印机
                                    // 如果备用打印机可用,直接转到备用用打印机打印
                                    // 如果备用打印机不可用, 则添加到打印列表继续等待打印, 直到任务超时
                                    LogUtils.debug(String.format("%s 打印失败", curPrintTask.getTaskId()));
                                    if (printErrorCount >= 3 && this.mBackupPrinter != null && this.mBackupPrinter.available()) {
                                        mEnableBackupPrinter = true;
                                        LogUtils.debug("启用备用打印机: " + mBackupPrinter.getPrinterName());
                                    }
                                    printTaskDeque.addFirst(curPrintTask);
                                }
                                // 开启了保持打印且没有切换到备用打印机
                                if (mEnabledKeepPrint && !mEnableBackupPrinter) {
                                    Thread.sleep(mErrorIntervalTime);
                                }
                                curPrintTask = null;
                            }
                        } else {
                            // 使用备用打印机打印当前的任务
                            if (!this.useBackupPrinter(curPrintTask)) {
                                // 如果备用打印机也打印失败, 则继续添加到打印队列中
                                LogUtils.debug("备用打印机打印失败, 关闭备用打印机");
                                mEnableBackupPrinter = false;
                                printTaskDeque.addFirst(curPrintTask);
                            }
                        }
                    } catch (Exception e) {
                        if (curPrintTask != null) {
                            // 当前打印任务超时
                            if (e instanceof PrintTimeoutException) {
                                printTaskTimeout(curPrintTask);
                                continue;
                            }
                            // 打印过程中出现不可逆的异常, 重试也不可能打印成功的, 直接触发回调
                            printTaskError(curPrintTask, "print error: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception ignored) {
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
        // 纸张大小变化标志位
        boolean paperChange = false;
        OnPaperChangeListener paperChangeLister = printTask.getPaperChangeLister();
        // 检测备用打印机和当前的打印机纸张大小是否一致, 如果不一致则触发纸张变化的回调, 可以让调用者重新设置打印模版
        if (paperChangeLister != null && getPaperWidth() != mBackupPrinter.getPaperWidth()) {
            paperChange = true;
            paperChangeLister.onChange(mBackupPrinter, printTask);
        }
        // 使用备用打印机进行打印, 需要设置成临时打印, 如果备用打印机也打印失败, 则不继续添加到备用打印机的打印队列中
        printTask.setTempPrint(true);
        LogUtils.debug(printTask.getTaskId() + String.format(" %s 转到 -> %s", this.toString(), mBackupPrinter.toString()));
        // 获取当前打印任务的回调
        OnPrintEventListener oldListener = printTask.getPrintEventListener();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        boolean finalPaperChange = paperChange;
        // 把打印任务的回调设置成自己的回调 , 然后再触发调用者的回调
        printTask.setPrintEventListener(new OnPrintEventListener() {
            @Override
            public void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj) {
                if (oldListener != null) {
                    oldListener.onEventTriggered(printer, printTask, event, obj);
                }
                mBackupPrinterResult = event == PrintEvent.SUCCESS;
                if (!mBackupPrinterResult && finalPaperChange) {
                    paperChangeLister.onChange(AbstractPrinter.this, printTask);
                }
                // 备用打印机打印结束
                countDownLatch.countDown();
            }
        });
        // 使用备用打印机进行打印
        mBackupPrinter.print(printTask);
        try {
            // 等待备用打印机打印结束
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
