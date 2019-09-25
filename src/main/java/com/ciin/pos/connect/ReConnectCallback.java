package com.ciin.pos.connect;

public interface ReConnectCallback {

    boolean condition();

    void onFailure(Throwable ex);
}
