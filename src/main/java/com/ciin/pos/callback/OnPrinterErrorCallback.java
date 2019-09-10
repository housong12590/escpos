package com.ciin.pos.callback;

import com.ciin.pos.connect.Connection;
import com.ciin.pos.printer.Printer;

public interface OnPrinterErrorCallback {

    void onConnectError(Printer printer, Connection connection);

    void onPrinterError(Printer printer);
}
