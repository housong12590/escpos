package com.cin.pos.printer;

import com.cin.pos.element.Document;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.parser.Template;
import com.cin.pos.util.LogUtils;
import com.cin.pos.util.Utils;

import java.util.concurrent.Callable;

public class PrintTask implements Callable<Void> {

    private String taskId;
    private Template template;
    private Object tag;
    private Printer printer;
    private long intervalTime;
    private long printTimeOut = 60 * 60 * 1000;
    private long createTime;


    public PrintTask(Template template) {
        this(Utils.generateId(), template);
    }

    public PrintTask(String taskId, Template template) {
        this.createTime = System.currentTimeMillis();
        this.taskId = taskId;
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public String getTaskId() {
        return taskId;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public long getPrintTimeOut() {
        return printTimeOut;
    }

    public void setPrintTimeOut(long printTimeOut) {
        this.printTimeOut = printTimeOut;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public boolean isTimeOut() {
        long nowTime = System.currentTimeMillis();
        return nowTime - createTime > printTimeOut;
    }


    @Override
    public String toString() {
        return this.taskId;
    }

    @Override
    public Void call() throws Exception {
        OrderSet orderSet = printer.getDevice().getOrderSet();
        printer.write(orderSet.reset());
        LogUtils.debug(String.format("%s 开始解析模版 ", taskId));
        Document document = template.toDocument();
        byte[] data = document.toBytes(printer.getDevice());
        LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", taskId, data.length));
        printer.write(data);
        printer.write(orderSet.paperFeed(5));
        printer.write(orderSet.cutPaper());
        if (printer.isBuzzer()) {
            printer.write(orderSet.buzzer(2));
        }
        printer.flush();
        return null;
    }


}
