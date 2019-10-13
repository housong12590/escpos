package com.ciin.pos;

import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.orderset.OrderSetFactory;
import com.ciin.pos.util.LogUtils;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Arrays;

public class Test {

    private static final String PING = "ping";
    private static final String PONG = "pong";


    public static void main(String[] args) throws NoSuchPortException, UnsupportedCommOperationException, PortInUseException, IOException {
//        OrderSet orderSet = OrderSetFactory.getDefault();
//        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM4");
//        CommPort commPort = portIdentifier.open("portName", Constants.SOCKET_TIMEOUT);
//        if (commPort instanceof SerialPort) {
//            SerialPort serialPort = (SerialPort) commPort;
//            //设置串口参数（波特率，数据位8，停止位1，校验位无）
//            serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//            LogUtils.debug("开启端口: " + serialPort.getName());
//            OutputStream os = serialPort.getOutputStream();
//            InputStream is = serialPort.getInputStream();
//            os.write(orderSet.status());
//            for (int i = 0; i < 2; i++) {
//                System.out.println("可读长度: " + is.available());
//                byte[] buff = new byte[is.available()];
//                is.read(buff);
//                System.out.println(Arrays.toString(buff));
//            }
//            Utils.safeClose(os, is);
//            serialPort.close();
//        }
//        test01();
//        test02();
        test03();
    }

    private static void test03() throws IOException {
        SocketConnection connection = new SocketConnection("192.168.10.60", 9100, 10000, false);
        connection.doConnect();
        byte[] buff = new byte[8];
        connection.read(buff);
    }

    private static void test02() {
        SocketConnection connection = new SocketConnection("192.168.10.60", 9100, 10000, false);
        connection.tryConnect();
        OrderSet orderSet = OrderSetFactory.getDefault();
        byte[] buff = new byte[48];
        try {
            int readLength = connection.writeAndRead(orderSet.status(), buff);
            LogUtils.warn("读取长度: " + readLength);
            LogUtils.warn("读取数据: " + Arrays.toString(buff));
        } catch (IOException e) {
        }
    }

    private static void test01() {
    }
}