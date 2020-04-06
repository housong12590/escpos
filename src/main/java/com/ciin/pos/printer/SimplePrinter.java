package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.listener.OnPrinterListener;

import java.util.HashSet;
import java.util.Set;

public abstract class SimplePrinter implements IPrinter {

    private Device device;
    private String printerName;
    private boolean buzzer;
    private Set<OnPrinterListener> listeners;

    public SimplePrinter(Device device) {
        this.device = device;
        this.listeners = new HashSet<>();
    }

    @Override
    public Device getDevice() {
        return device;
    }

    @Override
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    @Override
    public String getPrinterName() {
        return this.printerName;
    }

    @Override
    public int getPaperWidth() {
        return device.getPaperWidth();
    }

    @Override
    public void print(PrintTask printTask) {
        printTask.setPrinter(this);
    }

    @Override
    public void testPrint() {

    }

    @Override
    public void buzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    @Override
    public boolean isBuzzer() {
        return this.buzzer;
    }

    @Override
    public void addPrinterListener(OnPrinterListener printerListener) {
        this.listeners.add(printerListener);
    }

    @Override
    public void removePrinterListener(OnPrinterListener printerListener) {
        this.listeners.remove(printerListener);
    }

    abstract void print0(PrintTask printTask);
}
