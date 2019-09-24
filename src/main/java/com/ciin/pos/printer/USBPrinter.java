package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;

/**
 * usb驱动打印机
 */
public class USBPrinter extends AbstractPrinter {

    public USBPrinter(Device device) {
        super(device);
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        return false;
    }
}
