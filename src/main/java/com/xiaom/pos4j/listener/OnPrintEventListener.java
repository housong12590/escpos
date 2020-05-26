package com.xiaom.pos4j.listener;

import com.xiaom.pos4j.printer.PrintTask;
import com.xiaom.pos4j.printer.Printer;

/**
 * @author hous
 */
public interface OnPrintEventListener {

    void onEventTriggered(Printer printer, PrintTask printTask, PrintEvent event, Object obj);
}
