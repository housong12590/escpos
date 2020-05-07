package com.ciin.pos.listener;

import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;

public interface OnPaperChangeListener {

    void onChange(Printer printer, PrintTask printTask);
}
