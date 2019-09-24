package com.ciin.pos.printer;

import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;

/**
 * 并口打印机
 */
public class ParallelPortPrinter extends AbstractPrinter {

    public ParallelPortPrinter(Device device) {
        super(device);
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        return false;
    }
}
