package com.ciin.pos.printer;

public interface OnPrintEventListener {

    void onEvent(Printer printer, PrintTask printTask, PrintEvent event, Object obj);
}
