package com.ciin.pos.callback;

import com.ciin.pos.printer.Printer;

public interface OnPrinterErrorListener {

    void onConnectError(Printer printer, Throwable ex);

}