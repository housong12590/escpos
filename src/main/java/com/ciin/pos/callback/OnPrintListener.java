package com.ciin.pos.callback;

import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;

public interface OnPrintListener {

    void onSuccess(Printer printer, PrintTask printTask);

    void onError(Printer printer, PrintTask printTask, String errorMsg);
}
