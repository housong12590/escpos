package com.cin.pos.printer;

import com.cin.pos.callback.OnPrintCallback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrinterGroup implements OnPrintCallback {

    private Map<String, Printer> printerMap = new ConcurrentHashMap<>();

    private void registerPrinter(Printer printer) {
        String key = printer.getConnection().toString();
        printerMap.put(key, printer);
        printer.setOnPrintCallback(this);
    }

    private void unregisterPrinter(Printer printer) {
        String key = printer.getConnection().toString();
        printerMap.remove(key);
    }

    @Override
    public void onSuccess(Printer printer, Object tag) {

    }

    @Override
    public void onError(Printer printer, Object tag, Throwable e) {

    }
}
