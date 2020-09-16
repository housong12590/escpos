package com.hstmpl.escpos.listener;

import com.hstmpl.escpos.printer.Printer;

/**
 * @author hous
 */
public interface OnPrinterListener {

    void onPrinterError(Printer printer, Throwable ex);

}
