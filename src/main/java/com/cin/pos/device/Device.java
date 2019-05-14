package com.cin.pos.device;


import com.cin.pos.Constants;
import com.cin.pos.orderset.OrderSetFactory;
import com.cin.pos.orderset.OrderSet;

public class Device {


    private String charset = Constants.CHARSET_GBK;

    private Paper paper;

    private OrderSet orderSet = OrderSetFactory.getDefault();


    public Device(Paper paper) {
        this.paper = paper;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
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

    public static Device getDefault() {
        return new Device(new Paper_80());
    }
}
