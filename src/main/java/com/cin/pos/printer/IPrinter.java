package com.cin.pos.printer;

public interface IPrinter {

    void onCreate(Printer printer);

    void onDestroy(Printer printer);

    void onExecTask();

    void onConnentError(Throwable ex);

    void onPrintError(Throwable ex);


}
