package com.cin.pos.callback;

import com.cin.pos.printer.Printer;

public interface OnConnectionErrorCallback {

    void onConnectionError(Printer printer, Object tag);

}
