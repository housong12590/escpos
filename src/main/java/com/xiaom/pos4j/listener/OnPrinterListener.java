package com.xiaom.pos4j.listener;

import com.xiaom.pos4j.printer.Printer;

/**
 * @author hous
 */
public interface OnPrinterListener {

    void onPrinterError(Printer printer, Throwable ex);

}
