package com.cin.pos.callback;

import com.cin.pos.printer.PrintTask;
import com.cin.pos.printer.Printer;

public interface OnPrintCallback {

    void onSuccess(Printer printer, PrintTask printTask);

    void onError(Printer printer, PrintTask printTask, String errorMsg);

    void onCancel(Printer printer, PrintTask printTask);

    void onConnectError(Printer printer);

}
