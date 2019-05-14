package com.cin.pos.callback;

public interface OnPrintCallback {

    void onSuccess(Object tag);

    void onError(Object tag, Throwable e);

}
