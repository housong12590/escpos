package com.ciin.pos.printer1;

import com.ciin.pos.device.Device;

public class Printer {

    private Device device;
    private boolean isBuzzer;

    private Printer(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }

    public boolean isBuzzer() {
        return isBuzzer;
    }
}
