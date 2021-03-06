package com.ciin.pos.device;


import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.orderset.OrderSetFactory;

import java.nio.charset.Charset;

public class Device {

    private Charset charset = Charset.forName("GBK");

    private Paper paper;

    private OrderSet orderSet = OrderSetFactory.getDefault();

    public Device(Paper paper) {
        this.paper = paper;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public OrderSet getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(OrderSet orderSet) {
        this.orderSet = orderSet;
    }

    public int getPaperWidth() {
        return paper.paperWidth();
    }

    public static Device getDefault() {
        return DeviceFactory.device_80();
    }
}
