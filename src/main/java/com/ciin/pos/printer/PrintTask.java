package com.ciin.pos.printer;

import com.ciin.pos.listener.OnPrintTaskListener;
import com.ciin.pos.common.Dict;
import com.ciin.pos.element.Document;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

public class PrintTask {

    // 默认打印任务超时时间
    private static final int DEFAULT_PRINT_TIMEOUT = 60 * 60 * 1000;

    private String taskId;
    private Template template;
    private Object tag;
    private Printer printer;
    private long printTimeOut = DEFAULT_PRINT_TIMEOUT;
    private long createTime;
    private String title;
    private boolean tempPrint;
    private OnPrintTaskListener printTaskListener;

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

    public long getPrintTimeOut() {
        return printTimeOut;
    }

    public void setPrintTimeOut(long printTimeOut) {
        this.printTimeOut = printTimeOut;
    }

    public String getTitle() {
        return title;
    }

    public PrintTask setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isTempPrint() {
        return tempPrint;
    }

    public void setTempPrint(boolean tempPrint) {
        this.tempPrint = tempPrint;
    }

    public boolean isTimeout() {
        // 时间为0或者-1时,则永不超时
        if (printTimeOut == 0 || printTimeOut == -1) {
            return false;
        }
        long nowTime = System.currentTimeMillis();
        return nowTime - createTime > printTimeOut;
    }

    public void setPrintTaskListener(OnPrintTaskListener printTaskListener) {
        this.printTaskListener = printTaskListener;
    }

    public OnPrintTaskListener getPrintTaskListener() {
        return printTaskListener;
    }

    @Override
    public String toString() {
        return this.taskId;
    }

    public byte[] printData() throws TemplateParseException {
        ByteBuffer buffer = new ByteBuffer();
        if (printer == null) {
            throw new NullPointerException("请先调用PrintTask.setPrinter()绑定打印机再进行打印...");
        }
        // 获取打印指令集
        OrderSet orderSet = printer.getDevice().getOrderSet();
        // 初始化打印机
        buffer.write(orderSet.reset());
        LogUtils.debug(String.format("%s 开始解析模版 ", taskId));
        // 解析模版
        Document document = template.toDocument();
        // 写入打印数据
        buffer.write(document.toBytes(printer.getDevice()));
        // 进纸
        buffer.write(orderSet.paperFeed(5));
        // 切纸
        buffer.write(orderSet.cutPaper());
        // 设置蜂鸣声
        if (printer.isBuzzer()) {
            buffer.write(orderSet.buzzer(2));
        }
        return buffer.toByteArray();
    }
}
