package com.ciin.pos.printer;

import com.ciin.pos.connect.BlueToothConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.PlatformErrorException;
import com.ciin.pos.platform.Platform;
import com.ciin.pos.util.StringUtils;

public class BlueToothPrinter extends AbstractIOStreamPrinter {

    private String address;

    public BlueToothPrinter(String address) {
        this(Device.getDefault(), address);
    }

    public BlueToothPrinter(Device device, String address) {
        super(device, new BlueToothConnection(address));
        if (!Platform.isAndroid()) {
            this.close();
            throw new PlatformErrorException("not is android platform");
        }
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "蓝牙打印机: " + this.connection;
        }
        return printerName;
    }
}
