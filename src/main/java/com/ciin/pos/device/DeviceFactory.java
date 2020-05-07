package com.ciin.pos.device;

public class DeviceFactory {

    public static Device getDefault() {
        return new Device(new Paper_80());
    }

    public static Device device_80() {
        return new Device(new Paper_80());
    }

    public static Device device_58() {
        return new Device(new Paper_58());
    }

    public static Device device_76() {
        return new Device(new Paper_76());
    }
}
