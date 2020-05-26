package com.xiaom.pos4j.listener;

import com.xiaom.pos4j.printer.PrintTask;
import com.xiaom.pos4j.printer.Printer;

/**
 * @author hous
 */
public interface OnPaperChangeListener {

    void onChange(Printer printer, PrintTask printTask);
}
