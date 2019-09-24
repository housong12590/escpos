package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.ByteUtils;

import java.io.IOException;

/**
 * 网络转发打印机
 */
public class ForwardingPrinter extends AbstractPrinter {

    private final SocketConnection connection;

    public ForwardingPrinter(Device device, String host) {
        super(device);
        connection = new SocketConnection(host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT, false);
    }

    private boolean checkConnect() {
//        connection
        return true;
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        if (checkConnect()) {
            // 获取打印数据
            byte[] printData = printTask.printData();
            ByteBuffer buffer = new ByteBuffer();
            byte[] dataLength = ByteUtils.intToByteArray(printData.length);
            // 写入4字节的长度信息
            buffer.write(dataLength);
            // 写入data数据
            buffer.write(printData);
            try {
                connection.writeAndFlush(buffer.toByteArray());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
