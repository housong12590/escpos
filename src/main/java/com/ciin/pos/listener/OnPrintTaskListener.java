package com.ciin.pos.listener;

import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;

public interface OnPrintTaskListener {

    void onSuccess(Printer printer, PrintTask printTask);

    void onError(Printer printer, PrintTask printTask, String errorMsg);
}
