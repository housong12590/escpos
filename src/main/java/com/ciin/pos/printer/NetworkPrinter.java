package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.util.StringUtils;

/**
 * 网络打印机
 */
public class NetworkPrinter extends AbstractIOStreamPrinter {

    private String host;

    public NetworkPrinter(String host) {
        this(Device.getDefault(), host);
    }

    public NetworkPrinter(Device device, String host) {
        super(device, new SocketConnection(host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT, true));
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "网口打印机: " + this.connection;
        }
        return printerName;
    }
}
