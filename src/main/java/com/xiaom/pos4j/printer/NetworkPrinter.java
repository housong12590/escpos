package com.xiaom.pos4j.printer;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.connect.SocketConnection;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.util.StringUtils;

/**
 * 网络打印机
 * @author hous
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

    @Override
    public String toString() {
        return getPrinterName();
    }
}