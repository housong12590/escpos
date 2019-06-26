package com.cin.pos.printer;

import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.connect.Connection;
import com.cin.pos.convert.ConverterKit;
import com.cin.pos.device.Device;
import com.cin.pos.element.Document;
import com.cin.pos.element.Element;
import com.cin.pos.exception.ConnectException;
import com.cin.pos.exception.TimeoutException;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.parser.TemplateParse;
import com.cin.pos.util.LoggerUtil;

import java.util.Map;

public class PrintTaskRunnable implements Runnable {

    private Document document;
    private TemplateParse templateParse;
    private Object tag;
    private Printer printer;
    private long startTime;
    private String templateContent;
    private Map templateData;
    private int interval;
    private long createTime;
    private boolean blocking;
    private static final long invalidTime = 1800; // 打印超时时长

    PrintTaskRunnable(Object tag, Printer printer, Integer interval, boolean blocking) {
        this.tag = tag;
        this.printer = printer;
        this.interval = interval;
        this.createTime = System.currentTimeMillis() / 1000;
        this.blocking = blocking;
    }

    PrintTaskRunnable(Object tag, Printer printer) {
        this(tag, printer, 0, false);
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

    void setTemplateParse(TemplateParse templateParse, String templateContent, Map templateData) {
        this.templateParse = templateParse;
        this.templateContent = templateContent;
        this.templateData = templateData;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        if (templateParse != null) {
            try {
                LoggerUtil.debug(String.format("%s 开始解析模版...", printer.getConnection()));
                document = templateParse.parser(templateContent, templateData);
            } catch (Exception e) {
                printError(e);
                return;
            }
        }
        if (document == null) {
            printError(new NullPointerException("document can not null..."));
            return;
        }
        execPrint();
    }

    private void execPrint() {
        try {
            beforePrint();
            printDocument();
            afterPrint();
        } catch (ConnectException e) {
            if (!blocking) {
                printError(e);
            } else {
                long nowTime = System.currentTimeMillis() / 1000;
                if (nowTime - createTime > invalidTime) {
                    printError(new TimeoutException("打印超时,任务自动取消"));
                    return;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                execPrint();
            }
        }
    }

    private void beforePrint() throws ConnectException {
        Connection connection = printer.getConnection();
        if (connection == null) {
            throw new NullPointerException("printer connection can not null...");
        }
        if (!connection.isConnect()) {
//            LoggerUtil.debug(String.format("%s 正在连接打印机...", printer.getConnection()));
            connection.doConnect();
        }
        LoggerUtil.debug(String.format("%s %s 开始打印", printer.getConnection(), this.tag));
        OrderSet orderSet = printer.getDevice().getOrderSet();
        connection.write(orderSet.reset());
    }

    private void printDocument() throws ConnectException {
        Device device = printer.getDevice();
        Connection connection = printer.getConnection();
        int len = 0;
        for (Element element : document.getElements()) {
            byte[] bytes = ConverterKit.matchConverterToBytes(element, device);
            len += bytes.length;
            connection.write(bytes);
        }
        OrderSet orderSet = device.getOrderSet();
        connection.write(orderSet.paperFeed(5));
        connection.write(orderSet.cutPaper());
        connection.flush();
        LoggerUtil.debug(String.format("%s 发送打印指令长度为%s字节", printer.getConnection(), len));
    }

    private void afterPrint() throws ConnectException {
        Connection connection = printer.getConnection();
        OrderSet orderSet = printer.getDevice().getOrderSet();
        connection.write(orderSet.printEnd());
        printSuccess();
    }

    private void printError(Throwable e) {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        if (printCallback != null) {
            printCallback.onError(printer, this.tag, e);
        }
        LoggerUtil.error(String.format("%s %s 打印失败, %s", printer.getConnection(), this.tag, e.getMessage()));
    }

    private void printSuccess() {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        if (printCallback != null) {
            printCallback.onSuccess(printer, this.tag);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LoggerUtil.debug(String.format("%s %s 打印完成, 耗时%sms", printer.getConnection(), this.tag, elapsedTime));
    }
}
