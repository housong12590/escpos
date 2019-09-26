package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.device.Device;
import com.ciin.pos.util.LogUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.ParallelPort;

/**
 * 并口打印机
 */
public class ParallelPortPrinter extends AbstractLocalPortPrinter {

    private String portName;

    public ParallelPortPrinter(Device device, String portName) {
        super(device);
        this.portName = portName;
    }

    @Override
    CommPort openPort() throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        CommPort commPort = portIdentifier.open(portName, Constants.SOCKET_TIMEOUT);
        if (commPort instanceof ParallelPort) {
            ParallelPort serialPort = (ParallelPort) commPort;
            LogUtils.debug("开启端口: " + serialPort.getName());
            return serialPort;
        }
        throw new NoSuchPortException();
    }
}
