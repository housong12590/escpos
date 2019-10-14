package com.ciin.pos.printer;

import com.ciin.pos.connect.BlueToothConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;
import com.ciin.pos.util.StringUtils;

public class BlueToothPrinter extends IOStreamPrinter {

    private String address;

    public BlueToothPrinter(String address) {
        this(DeviceFactory.getDefault(), address);
    }

    public BlueToothPrinter(Device device, String address) {
        super(device, new BlueToothConnection(address));
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "蓝牙打印机:" + this.connection;
        }
        return printerName;
    }
}
