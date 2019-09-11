package com.ciin.pos.device;

public class DeviceFactory {

    public static Device getDefault() {
        return new Device(new Paper_80());
    }
}
