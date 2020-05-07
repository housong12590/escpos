package com.ciin.pos.listener;

import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;

public interface OnPrintEventListener {

    void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj);
}
