package com.ciin.pos.printer1;

import com.ciin.pos.common.Dict;
import com.ciin.pos.device.Device;
import com.ciin.pos.element.Document;
import com.ciin.pos.exception.TemplateException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.parser.Template;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.IdUtils;

public class PrintTask {

    private Long timeout;
    private String taskId;
    private String title;
    private Template template;
    private Printer printer;

    public PrintTask(Template template) {
        this(IdUtils.uuid(), template, null);
    }

    public PrintTask(String taskId, String templateStr, Dict templateDate, Object tag) {
        this(taskId, new Template(templateStr, templateDate), tag);
    }

    public PrintTask(String taskId, Template template, Object tag) {
        this.taskId = taskId;
        this.template = template;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    private byte[] getPrintData() throws TemplateException {
        if (template == null) {
            throw new TemplateException("template can not null");
        }
        Device device = printer.getDevice();
        Document document = template.toDocument();
        // 解析打印模板
        byte[] printData = document.toBytes(device);
        ByteBuffer buffer = new ByteBuffer();
        OrderSet orderSet = device.getOrderSet();
        // 初始化打印机
        buffer.write(orderSet.reset());
        // 写入打印数据
        buffer.write(printData);
        // 进纸
        buffer.write(orderSet.paperFeed(5));
        // 切纸
        buffer.write(orderSet.cutPaper());
        // 设置打印机蜂鸣声
        if (printer.isBuzzer()) {
            buffer.write(orderSet.buzzer(2));
        }
        return buffer.toByteArray();
    }
}
