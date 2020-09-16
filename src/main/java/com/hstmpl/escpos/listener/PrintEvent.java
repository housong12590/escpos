package com.hstmpl.escpos.listener;

/**
 * @author hous
 */
public enum PrintEvent {

    /**
     * 打印成功
     */
    SUCCESS("打印成功"),

    /**
     * 打印失败
     */
    ERROR("打印失败"),

    /**
     * 打印超时
     */
    TIMEOUT("打印超时"),

    /**
     * 取消打印
     */
    CANCEL("取消打印");


    String value;

    PrintEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
