package com.ciin.pos.listener;

import com.ciin.pos.printer.Printer;

public interface OnPrinterListener {

    void onPrinterError(Printer printer, Throwable ex);

}
