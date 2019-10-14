package com.ciin.pos.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueToothConnection implements Connection {


    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private String address;
    private InputStream is;
    private OutputStream os;
    private boolean isConnect;

    public BlueToothConnection(String address) {
        this.address = address;
    }

    @Override
    public void doConnect() throws IOException {
        try {
            adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                throw new IOException("不支持蓝牙服务");
            }
            adapter.cancelDiscovery();
            if (adapter.isEnabled()) {
                throw new IOException("蓝牙服务未打开");
            }
            if (!BluetoothAdapter.checkBluetoothAddress(address)) {
                throw new IOException("蓝牙地址无效");
            }
            this.device = adapter.getRemoteDevice(address);
            this.socket = device.createInsecureRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
            this.socket.connect();
            is = socket.getInputStream();
            os = socket.getOutputStream();
            isConnect = true;
            LogUtils.debug("连接成功 " + this);
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public boolean tryConnect() {
        return tryConnect(null);
    }

    @Override
    public boolean tryConnect(OnConnectCallback callback) {
        if (!isConnect) {
            try {
                doConnect();
                return true;
            } catch (IOException e) {
                isConnect = false;
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        }
        return false;
    }

    @Override
    public boolean isConnect() {
        return isConnect;
    }

    @Override
    public void write(byte[] data) throws IOException {
        try {
            os.write(data);
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public void writeAndFlush(byte[] data) throws IOException {
        write(data);
        flush();
    }

    @Override
    public int writeAndRead(byte[] wb, byte[] rb) throws IOException {
        writeAndFlush(wb);
        return read(rb);
    }

    @Override
    public void flush() throws IOException {
        try {
            os.flush();
        } catch (IOException e) {
            this.isConnect = false;
            throw e;
        }
    }

    @Override
    public int read(byte[] buff) throws IOException {
        try {
            return is.read(buff);
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public void close() {
        Utils.safeClose(is, os, socket);
    }

    @Override
    public String toString() {
        return "blueTooth " + this.address;
    }
}
