package com.hstmpl.escpos.printer;

import com.hstmpl.escpos.connect.AndroidBlueToothConnection;
import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.exception.PlatformException;
import com.hstmpl.escpos.platform.Platform;
import com.hstmpl.escpos.util.StringUtils;

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
