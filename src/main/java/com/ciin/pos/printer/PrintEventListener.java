package com.ciin.pos.printer;

public interface PrintEventListener {

    void onEvent(Printer printer, PrintEvent event);
}
