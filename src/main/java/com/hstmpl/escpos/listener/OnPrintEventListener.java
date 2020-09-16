package com.hstmpl.escpos.listener;

import com.hstmpl.escpos.printer.PrintTask;
import com.hstmpl.escpos.printer.Printer;

/**
 * @author hous
 */
public interface OnPrintEventListener {

    void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj);
}
