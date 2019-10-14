package com.ciin.pos.printer;

import android.bluetooth.BluetoothAdapter;

import com.ciin.pos.device.Device;
import com.ciin.pos.util.LogUtils;

public class BluetoothPrinter extends AbstractPrinter {

    public BluetoothPrinter(Device device) {
        super(device);
    }

    @Override
    protected boolean print0(PrintTask printTask) throws Exception {
        return false;
    }

    @Override
    public boolean available() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            LogUtils.warn("不支持蓝牙");
            return false;
        }
        if (!defaultAdapter.isEnabled()) {
            LogUtils.error("蓝牙未打开");
            return false;
        }


        return false;
    }
}
