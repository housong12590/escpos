package com.hstmpl.escpos.printer;

import com.hstmpl.escpos.Constants;
import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.exception.PlatformException;
import com.hstmpl.escpos.platform.Platform;
import com.hstmpl.escpos.util.LogUtils;
import com.hstmpl.escpos.util.StringUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

/**
 * 串口打印机
 * @author hous
 */
public class SerialPortPrinter extends AbstractCommPortPrinter {

    private String portName;
    private int baudRate;

    public SerialPortPrinter(String portName, int baudRate) {
        this(Device.getDefault(), portName, baudRate);
    }

    public SerialPortPrinter(Device device, String portName, int baudRate) {
        super(device);
        this.portName = portName;
        this.baudRate = baudRate;
        if (!Platform.isWindows()) {
            this.close();
            throw new PlatformException("not is windows platform");
        }
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


    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "串口打印机: " + portName;
        }
        return printerName;
    }
}


