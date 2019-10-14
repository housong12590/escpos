package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.util.StringUtils;

public class NetworkPrinter1 extends IOStreamPrinter {

    private String host;

    public NetworkPrinter1(String host) {
        this(Device.getDefault(), host);
    }

    public NetworkPrinter1(Device device, String host) {
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
            return "网口打印机:" + this.connection;
        }
        return printerName;
    }
}
