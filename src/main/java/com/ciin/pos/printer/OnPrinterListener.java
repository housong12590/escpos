package com.ciin.pos.printer;

import com.ciin.pos.listener.PrintEvent;

public interface OnPrinterListener {

    void on(IPrinter printer, PrintEvent event, PrintTask1 printTask);
}
