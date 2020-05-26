package com.xiaom.pos4j.printer;

import com.xiaom.pos4j.connect.AndroidBlueToothConnection;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.exception.PlatformException;
import com.xiaom.pos4j.platform.Platform;
import com.xiaom.pos4j.util.StringUtils;

/**
 * @author hous
 */
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
