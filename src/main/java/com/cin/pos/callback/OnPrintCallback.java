package com.cin.pos.callback;

import com.cin.pos.printer.Printer;

public interface OnPrintCallback {

    void onSuccess(Printer printer, Object tag);

    void onError(Printer printer, Object tag, Throwable e);

}
