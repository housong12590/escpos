package com.cin.pos.util;

import java.util.LinkedList;
import java.util.List;

public class ByteBuffer {

    private List<byte[]> bytesList = new LinkedList<>();
    private int length = 0;

    public void write(byte[] bytes) {
        bytesList.add(bytes);
        length += bytes.length;
    }

    public void write(byte b) {
        write(new byte[]{b});
    }

    public byte[] toByteArray() {
        byte[] allBytes = new byte[length];
        int pos = 0;
        for (byte[] bytes : bytesList) {
            System.arraycopy(bytes, 0, allBytes, pos, bytes.length);
            pos += bytes.length;
        }
        return allBytes;
    }

    public int length() {
        return length;
    }
}
