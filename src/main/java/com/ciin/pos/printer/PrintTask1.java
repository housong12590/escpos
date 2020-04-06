package com.ciin.pos.printer;

import com.ciin.pos.element.Document;
import com.ciin.pos.exception.TemplateException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.IdUtils;
import com.ciin.pos.util.LogUtils;

public class PrintTask1 {

    private IPrinter printer;
    private String taskId;
    private Template template;
    private String title;
    private Object attach;

    public PrintTask1(Template template) {
        this(IdUtils.uuid(), template);
    }

    public PrintTask1(String taskId, Template template) {
        this.taskId = taskId;
        this.template = template;
    }

    public void setPrinter(IPrinter printer) {
        this.printer = printer;
    }

    public void setAttach(Object attach) {
        this.attach = attach;
    }

    public Object getAttach() {
        return attach;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public byte[] printData() throws TemplateException {
        ByteBuffer buffer = new ByteBuffer();
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
