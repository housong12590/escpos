package com.cin.pos.printer;

import com.cin.pos.callback.OnConnectionErrorCallback;
import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.element.Document;
import com.cin.pos.exception.ConnectException;
import com.cin.pos.exception.TimeoutException;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.parser.PrintTemplate;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.Utils;

import java.util.Map;

public class PrintTaskRunnable implements Runnable {

    private Document document;
    private PrintTemplate printTemplate;
    private Object tag;
    private Printer printer;
    private long startTime;
    private int interval;
    private long createTime;

    PrintTaskRunnable(Object tag, Printer printer, Integer interval) {
        this.tag = tag;
        this.printer = printer;
        this.createTime = System.currentTimeMillis() / 1000;
        this.interval = interval;
    }

    PrintTaskRunnable(Object tag, Printer printer) {
        this(tag, printer, 0);
    }

    public long getCreateTime() {
        return createTime;
    }

    public Document getDocument() {
        return document;
    }

    void setDocument(Document document) {
        this.document = document;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    void setTemplateParse(PrintTemplate printTemplate, String templateContent, Map templateData) {
        this.printTemplate = printTemplate;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        if (printTemplate != null) {
            try {
                LoggerUtils.debug(String.format("%s %s 开始解析模版...", printer.getConnection(), tag));
                this.document = printTemplate.toDocument();
            } catch (Exception e) {
                printError(e);
                return;
            }
        }
        if (document == null) {
            printError(new NullPointerException("document can not null..."));
            return;
        }
        LoggerUtils.debug(String.format("%s %s 模版解析完成, 准备发送打印数据", printer.getConnection(), tag));
        execPrint();
    }

    private void execPrint() {
        try {
            beforePrint();
            printDocument();
            afterPrint();
        } catch (ConnectException e) {
            if (!this.printer.isBlocking()) {
                printError(e);
            } else {
                connectionError();
                LoggerUtils.error(String.format("%s %s 打印机连接失败, 稍后尝试重新连接...", printer.getConnection(), this.tag));
                long nowTime = System.currentTimeMillis() / 1000;
                if (nowTime - createTime > printer.getPrinterTimeOut()) {
                    printError(new TimeoutException("打印机连接超时, 任务自动取消"));
                    return;
                }
                Utils.sleep(5000);
                execPrint();
            }
        }
    }

    private void connectionError() {
        OnConnectionErrorCallback errorCallback = printer.getConnectionErrorCallback();
        if (errorCallback != null) {
            errorCallback.onConnectionError(printer, this.tag);
        }
    }

    private void beforePrint() throws ConnectException {
        Connection connection = printer.getConnection();
        if (connection == null) {
            throw new NullPointerException("printer connection can not null...");
        }
        if (!connection.isConnect()) {
            connection.doConnect();
        }
        LoggerUtils.debug(String.format("%s %s 开始打印", printer.getConnection(), this.tag));
        OrderSet orderSet = printer.getDevice().getOrderSet();
        connection.write(orderSet.reset());
    }

    private void printDocument() throws ConnectException {
        Device device = printer.getDevice();
        Connection connection = printer.getConnection();
        byte[] data = document.toBytes(device);
        connection.write(data);
        OrderSet orderSet = device.getOrderSet();
        connection.write(orderSet.paperFeed(5));
        connection.write(orderSet.cutPaper());
        LoggerUtils.debug(String.format("%s %s 发送打印指令长度为%s字节", printer.getConnection(), tag, data.length));
    }

    private void afterPrint() throws ConnectException {
        Connection connection = printer.getConnection();
        OrderSet orderSet = printer.getDevice().getOrderSet();
        if (printer.isBel()) {
            connection.write(orderSet.printEnd());
        }
        connection.flush();
        printSuccess();
    }

    private void printError(Throwable e) {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        LoggerUtils.error(String.format("%s %s 打印失败, %s", printer.getConnection(), this.tag, e.getMessage()));
        if (printCallback != null) {
            printCallback.onError(printer, this.tag, e);
        }
    }

    private void printSuccess() {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LoggerUtils.debug(String.format("%s %s 打印完成, 耗时%sms", printer.getConnection(), this.tag, elapsedTime));
        if (printCallback != null) {
            printCallback.onSuccess(printer, this.tag);
        }
    }
}
