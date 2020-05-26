package com.xiaom.pos4j.printer;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.connect.SocketConnection;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.exception.PrintTimeoutException;
import com.xiaom.pos4j.protocol.PrintProtocol;
import com.xiaom.pos4j.util.LogUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 网络转发打印机
 * @author hous
 */
public class ForwardingPrinter extends AbstractPrinter {

    private final SocketConnection connection;
    private DataInputStream is;
    private static final String PING = "ping";
    private static byte[] hb;
    private String printerValue;
    private String host;
    private int port;

    static {
        byte[] data = PING.getBytes(StandardCharsets.UTF_8);
        PrintProtocol protocol = new PrintProtocol(PrintProtocol.ping_head, data);
        hb = protocol.toBytes();
    }

    public ForwardingPrinter(Device device, String host, String printerValue) {
        super(device);
        this.host = host;
        this.port = Constants.PRINTER_PORT;
        this.printerValue = printerValue;
        connection = new SocketConnection(this.host, this.port, Constants.SOCKET_TIMEOUT, false);
    }

    private boolean checkConnect() {
        if (!connection.isConnect()) {
            return false;
        }
        try {
            connection.writeAndFlush(hb);
            PrintProtocol data = readData();
            if (data != null) {
                if (data.getHead() == PrintProtocol.ping_head) {
                    return true;
                }
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    @Override
    protected boolean print0(PrintTask printTask) throws Exception {
        if (printTask.isTimeout()) {
            throw new PrintTimeoutException();
        }
        // 获取打印数据
        byte[] data = printTask.printData();
        PrintProtocol pr = new PrintProtocol(PrintProtocol.print_head, data);
        connection.writeAndFlush(pr.toBytes());
        LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
        return checkResult(printTask, readData());
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "转发打印机";
        }
        return printerName;
    }

    @Override
    protected void printEnd0() {
        this.connection.close();
        if (connection.isConnect()) {
            LogUtils.debug(String.format("连接断开 %s", this.connection));
        }
    }

    @Override
    public boolean available() {
        if (!connection.isConnect()) {
            doTryConnect();
        }
        return checkConnect();
    }

    private void doTryConnect() {
        if (connection.tryConnect()) {
            is = new DataInputStream(connection.getInputStream());
            try {
                // 连接完成后, 初始化打印机信息
                byte[] data = printerValue.getBytes(StandardCharsets.UTF_8);
                PrintProtocol protocol = new PrintProtocol(PrintProtocol.printer_head, data);
                connection.writeAndFlush(protocol.toBytes());
            } catch (IOException ignored) {

            }
        }
    }

    private boolean checkResult(PrintTask printTask, PrintProtocol protocol) {
        if (protocol != null && protocol.getHead() == PrintProtocol.result_head) {
            String resultJson = new String(protocol.getData());
            Dict dict = Dict.create(resultJson);
            if (dict.getBool("error")) {
                throw new RuntimeException(dict.getString("msg"));
            } else if (dict.getBool("success")) {
                return true;
            } else {
                LogUtils.warn(printTask.getTaskId() + " " + dict.getString("msg"));
            }
        }
        return false;
    }

    private PrintProtocol readData() {
        try {
            byte head = is.readByte();
            int length = is.readInt();
            byte[] data = new byte[length];
            is.read(data);
            return new PrintProtocol(head, data);
        } catch (IOException ignored) {

        }
        return null;
    }

    public String getHost() {
        return host;
    }

    public ForwardingPrinter setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ForwardingPrinter setPort(int port) {
        this.port = port;
        return this;
    }
}
