package com.ciin.pos.printer;

import com.ciin.pos.callback.OnPrintTaskCallback;
import com.ciin.pos.callback.OnPrinterErrorCallback;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.device.Device;

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
    protected Connection mConnection;

    AbstractPrinter(Device device, Connection connection) {
        this.mDevice = device;
        this.mConnection = connection;
        printTaskQueue = new ConcurrentLinkedQueue<>();
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
    public Connection getConnection() {
        return mConnection;
    }

    @Override
    public abstract void print(PrintTask printTask);

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

    @Override
    public abstract void release();
}
