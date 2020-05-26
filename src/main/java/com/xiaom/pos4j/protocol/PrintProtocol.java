package com.xiaom.pos4j.protocol;

import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.util.ByteUtils;

/**
 * @author hous
 */
public class PrintProtocol {

    public static final byte print_head = 0x1b;
    public static final byte ping_head = 0x01;
    public static final byte printer_head = 0x02;
    public static final byte result_head = 0x03;

    private byte head;
    private int length;
    private byte[] data;

    public PrintProtocol(byte head, byte[] data) {
        this.head = head;
        this.data = data;
        this.length = data.length;
    }

    public byte getHead() {
        return head;
    }

    public PrintProtocol setHead(byte head) {
        this.head = head;
        return this;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = new ByteBuffer();
        buffer.write(this.head);
        buffer.write(ByteUtils.intToBytes(length));
        buffer.write(data);
        return buffer.toByteArray();
    }
}
