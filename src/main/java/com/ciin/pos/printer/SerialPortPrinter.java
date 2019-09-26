package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.device.Device;
import com.ciin.pos.util.LogUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

/**
 * 串口打印机
 */
public class SerialPortPrinter extends AbstractLocalPortPrinter {

    private String portName;
    private int baudRate;

    public SerialPortPrinter(Device device, String portName, int baudRate) {
        super(device);
        this.portName = portName;
        this.baudRate = baudRate;
    }

    @Override
    CommPort openPort() throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        CommPort commPort = portIdentifier.open(portName, Constants.SOCKET_TIMEOUT);
        if (commPort instanceof SerialPort) {
            SerialPort serialPort = (SerialPort) commPort;
            //设置串口参数（波特率，数据位8，停止位1，校验位无）
            serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            LogUtils.debug("开启端口: " + serialPort.getName());
            return serialPort;
        }
        throw new NoSuchPortException();
    }
}
