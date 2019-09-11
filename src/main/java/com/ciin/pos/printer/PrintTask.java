package com.ciin.pos.printer;

import com.ciin.pos.common.Dict;
import com.ciin.pos.element.Document;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.util.concurrent.Callable;

public class PrintTask implements Callable<Void> {

    // 默认打印任务超时时间
    private static final int DEFAULT_PRINT_TIMEOUT = 60 * 60 * 1000;

    private String taskId;
    private Template template;
    private Object tag;
    private Printer printer;
    private long intervalTime;
    private long printTimeOut = DEFAULT_PRINT_TIMEOUT;
    private long createTime;


    public PrintTask(Template template) {
        this(Utils.generateId(), template);
    }

    public PrintTask(String taskId, Template template) {
        this.createTime = System.currentTimeMillis();
        this.taskId = taskId;
        this.template = template;
    }

    public PrintTask(String taskId, String templateStr, Dict templateData, Object tag) {
        this.template = new Template(templateStr, templateData);
        this.taskId = taskId;
        this.createTime = System.currentTimeMillis();
        this.tag = tag;
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
        if (printer == null) {
            throw new NullPointerException("请先调用PrintTask.setPrinter()绑定打印机再进行打印...");
        }
        // 获取打印指令集
        OrderSet orderSet = printer.getDevice().getOrderSet();
        // 初始化打印机
        printer.write(orderSet.reset());
        LogUtils.debug(String.format("%s 开始解析模版 ", taskId));
        // 解析模版
        Document document = template.toDocument();
        // 转换成字节数组
        byte[] data = document.toBytes(printer.getDevice());
        LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", taskId, data.length));
        // 写入打印数据
        printer.write(data);
        // 进纸
        printer.write(orderSet.paperFeed(5));
        // 切纸
        printer.write(orderSet.cutPaper());
        // 设置蜂鸣声
        if (printer.isBuzzer()) {
            printer.write(orderSet.buzzer(2));
        }
        // 冲刷数据
        printer.flush();
        return null;
    }
}
