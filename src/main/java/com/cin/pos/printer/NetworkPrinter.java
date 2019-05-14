package com.cin.pos.printer;

import com.cin.pos.Constants;
import com.cin.pos.connect.SocketConnection;
import com.cin.pos.device.Device;

public class NetworkPrinter extends Printer {


    public NetworkPrinter(String hostname) {
        this(hostname, Constants.PRINTER_PORT);
    }

    public NetworkPrinter(String hostname, int port) {
        this(hostname, port, Constants.SOCKET_TIMEOUT);
    }

    public NetworkPrinter(String hostname, int port, int timeout) {
        super(new SocketConnection(hostname, port, timeout),
                Device.getDefault());
    }
}
