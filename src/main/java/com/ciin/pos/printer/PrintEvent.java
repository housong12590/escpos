package com.ciin.pos.printer;

public enum PrintEvent {

    /**
     * 打印成功
     */
    SUCCESS,

    /**
     * 打印失败
     */
    ERROR,

    /**
     * 打印超时
     */
    TIMEOUT,

    /**
     * 取消打印
     */
    CANCEL

}
