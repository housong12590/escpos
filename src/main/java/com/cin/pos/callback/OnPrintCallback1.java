package com.cin.pos.callback;

import com.cin.pos.printer.PrintTask;
import com.cin.pos.printer.Printer1;

public interface OnPrintCallback1 {

    void onSuccess(Printer1 printer, PrintTask printTask);

    void onError(Printer1 printer, PrintTask printTask, String errorMsg);

    void onCancel(Printer1 printer, PrintTask printTask);

    void onConnectError(Printer1 printer, PrintTask printTask);

}
