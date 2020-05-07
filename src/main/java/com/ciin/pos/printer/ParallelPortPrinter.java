package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.PlatformException;
import com.ciin.pos.platform.Platform;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.ParallelPort;

/**
 * 并口打印机
 */
public class ParallelPortPrinter extends AbstractCommPortPrinter {

    private String portName;

    public ParallelPortPrinter(String portName) {
        this(Device.getDefault(), portName);
    }

    public ParallelPortPrinter(Device device, String portName) {
        super(device);
        if (!Platform.isWindows()) {
            this.close();
            throw new PlatformException("not is windows platform");
        }
        this.portName = portName;
    }

    @Override
    CommPort openPort() throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        CommPort commPort = portIdentifier.open(portName, Constants.SOCKET_TIMEOUT);
        if (commPort instanceof ParallelPort) {
            ParallelPort parallelPort = (ParallelPort) commPort;
            LogUtils.debug("开启端口: " + parallelPort.getName());
            return parallelPort;
        }
        throw new NoSuchPortException();
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "并口打印机: " + portName;
        }
        return printerName;
    }
}
