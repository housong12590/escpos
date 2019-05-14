package com.cin.pos.printer;

import com.cin.pos.connect.Connection;
import com.cin.pos.device.Device;
import com.cin.pos.convert.ConverterKit;
import com.cin.pos.element.Element;
import com.cin.pos.parser.TemplateParse;
import com.cin.pos.util.LoggerUtil;
import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.element.Document;
import com.cin.pos.orderset.OrderSet;

import java.io.IOException;
import java.util.Map;

public class PrintTaskRunnable implements Runnable {

    private Document document;
    private TemplateParse templateParse;
    private Object tag;
    private Printer printer;
    private long startTime;
    private String templateContent;
    private Map templateData;

    PrintTaskRunnable(Object tag, Printer printer) {
        this.tag = tag;
        this.printer = printer;
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

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        if (templateParse != null) {
            try {
                LoggerUtil.debug("开始解析模版...");
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
        try {
            beforePrint();
            printDocument();
            afterPrint();
        } catch (IOException e) {
            printError(e);
        }
    }

    private void beforePrint() throws IOException {
        Connection connection = printer.getConnection();
        if (connection == null) {
            throw new NullPointerException("printer connection can not null...");
        }
        if (!connection.isConnect()) {
            LoggerUtil.debug(String.format("%s 正在连接打印机...", connection));
            connection.doConnect();
        }
        LoggerUtil.debug(String.format("%s 开始打印", this.tag));
        OrderSet orderSet = printer.getDevice().getOrderSet();
        connection.write(orderSet.reset());
    }

    private void printDocument() throws IOException {
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
        LoggerUtil.debug(String.format("发送打印指令长度为%s字节", len));
    }

    private void afterPrint() throws IOException {
        Connection connection = printer.getConnection();
        OrderSet orderSet = printer.getDevice().getOrderSet();
        connection.write(orderSet.printEnd());
        printSuccess();
    }

    private void printError(Throwable e) {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        if (printCallback != null) {
            printCallback.onError(this.tag, e);
        }
        LoggerUtil.error(String.format("%s 打印失败, %s", this.tag, e.getMessage()));
    }

    private void printSuccess() {
        OnPrintCallback printCallback = printer.getOnPrintCallback();
        if (printCallback != null) {
            printCallback.onSuccess(this.tag);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        LoggerUtil.debug(String.format("%s 打印完成, 耗时%sms", this.tag, elapsedTime));
    }
}
