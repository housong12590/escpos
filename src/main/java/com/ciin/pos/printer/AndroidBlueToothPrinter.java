package com.ciin.pos.printer;

import com.ciin.pos.connect.AndroidBlueToothConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.PlatformException;
import com.ciin.pos.platform.Platform;
import com.ciin.pos.util.StringUtils;

public class AndroidBlueToothPrinter extends AbstractIOStreamPrinter {

    private String address;

    public AndroidBlueToothPrinter(String address) {
        this(Device.getDefault(), address);
    }

    public AndroidBlueToothPrinter(Device device, String address) {
        super(device, new AndroidBlueToothConnection(address));
        if (!Platform.isAndroid()) {
            this.close();
            throw new PlatformException("not is android platform");
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
