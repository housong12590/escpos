package com.hstmpl.escpos.listener;

import com.hstmpl.escpos.printer.PrintTask;
import com.hstmpl.escpos.printer.Printer;

/**
 * @author hous
 */
public interface OnPaperChangeListener {

    void onChange(Printer printer, PrintTask printTask);
}
