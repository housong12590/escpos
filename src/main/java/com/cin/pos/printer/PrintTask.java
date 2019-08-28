package com.cin.pos.printer;

import com.cin.pos.element.Document;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.parser.Template;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.Utils;

import java.util.concurrent.Callable;

public class PrintTask implements Callable<Void> {

    private String taskId;
    private Template template;
    private Object tag;
    private Printer1 printer;
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

    public Printer1 getPrinter() {
        return printer;
    }

    public void setPrinter(Printer1 printer) {
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

    @Override
    public String toString() {
        return this.taskId;
    }

    @Override
    public Void call() throws Exception {
        OrderSet orderSet = printer.getDevice().getOrderSet();
        printer.write(orderSet.reset());
        LoggerUtils.debug(String.format("%s 开始解析模版 ", taskId));
        Document document = template.toDocument();
        byte[] data = document.toBytes(printer.getDevice());
        LoggerUtils.debug(String.format("%s 发送打印数据%s字节 ", taskId, data.length));
        printer.write(data);
        if (printer.isBuzzer()) {
            printer.write(orderSet.printEnd());
        }
        printer.flush();
        return null;
    }


}
