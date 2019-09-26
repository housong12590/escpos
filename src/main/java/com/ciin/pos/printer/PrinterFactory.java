package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;

public class PrinterFactory {

    public enum PrinterType {
        network,
        drive,
        forwarding,
        parallel,
        serial
    }

    public static Printer getPrinter(PrinterType type, String value) {
        Device device = DeviceFactory.getDefault();
        switch (type) {
            case network:
                return new NetworkPrinter(device, value);
            case drive:
                return new DrivePrinter(device, value);
            case serial:
                return new SerialPortPrinter(device, value, 9600);
            case parallel:
                return new ParallelPortPrinter(device, value);
            case forwarding:
                return new ForwardingPrinter(device, value);
        }
        return null;
    }
}
