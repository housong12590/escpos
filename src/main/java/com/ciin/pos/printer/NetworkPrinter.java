package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;

public class NetworkPrinter extends Printer {


    private String host;
    private int port;
    private int timeout;

    public NetworkPrinter(String hostname) {
        this(hostname, Constants.PRINTER_PORT);
    }

    public NetworkPrinter(String hostname, int port) {
        this(hostname, port, Constants.SOCKET_TIMEOUT);
    }

    public NetworkPrinter(String hostname, int port, int timeout) {
        super(new SocketConnection(hostname, port, timeout),
                Device.getDefault());
        this.host = hostname;
        this.port = port;
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
