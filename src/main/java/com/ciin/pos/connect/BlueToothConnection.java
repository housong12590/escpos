package com.ciin.pos.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.ciin.pos.exception.BlueToothException;
import com.ciin.pos.util.Utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueToothConnection extends AbstractConnection {

    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int bufferSize = 4096;
    private BluetoothSocket socket;
    private String address;
    private InputStream is;
    private OutputStream os;
    private boolean useBuff;

    public BlueToothConnection(String address) {
        this(address, false);
    }

    public BlueToothConnection(String address, boolean useBuff) {
        this.address = address;
        this.useBuff = useBuff;
    }

    @Override
    public void connect() throws IOException {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            throw new BlueToothException("不支持蓝牙服务");
        }
        adapter.cancelDiscovery();
        if (!adapter.isEnabled()) {
            throw new BlueToothException("蓝牙服务未打开");
        }
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            throw new BlueToothException("蓝牙MAC地址无效");
        }
        BluetoothDevice device = adapter.getRemoteDevice(address);
        this.socket = device.createInsecureRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
        this.socket.connect();
        is = socket.getInputStream();
        if (useBuff) {
            os = new BufferedOutputStream(socket.getOutputStream(), bufferSize);
        } else {
            os = socket.getOutputStream();
        }
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }

    @Override
    public void close() {
        super.close();
        Utils.safeClose(socket);
    }

    @Override
    public String toString() {
        return "蓝牙: " + this.address;
    }
}
