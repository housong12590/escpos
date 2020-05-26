package com.xiaom.pos4j.util;

/**
 * @author hous
 */
public class ByteUtils {


    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    public static byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            bytes[i] = (byte) ((value >> offset) & 0xff);
        }
        return bytes;
    }

    public static long bytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value <<= 8;
            value |= (bytes[i] & 0xff);
        }
        return value;
    }

    public static byte boolToByte(boolean value) {
        return (byte) (value ? 0x01 : 0x00);
    }

    public static boolean byteToBool(byte b) {
        return b == 0x01;
    }

    public static byte[] shortToBytes(Short value) {
        int temp = value;
        byte[] bytes = new byte[2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return bytes;
    }

    public static short byteToShort(byte[] bytes) {
        short s0 = (short) (bytes[0] & 0xff);
        short s1 = (short) (bytes[1] & 0xff);
        s1 <<= 8;
        return (short) (s0 | s1);
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] stringToBytes(String value) {
        return value.getBytes();
    }

    public static byte[] doubleToBytes(double value) {
        byte[] bytes = new byte[8];
        long l = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            bytes[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return bytes;
    }

    public static double bytesToDouble(byte[] bytes) {
        long value;
        value = bytes[0];
        value &= 0xff;
        value |= ((long) bytes[1] << 8);
        value &= 0xffff;
        value |= ((long) bytes[2] << 16);
        value &= 0xffffff;
        value |= ((long) bytes[3] << 24);
        value &= 0xffffffffL;
        value |= ((long) bytes[4] << 32);
        value &= 0xffffffffffL;
        value |= ((long) bytes[5] << 40);
        value &= 0xffffffffffffL;
        value |= ((long) bytes[6] << 48);
        value &= 0xffffffffffffffL;
        value |= ((long) bytes[7] << 56);
        return Double.longBitsToDouble(value);
    }

    public static float bytesToFloat(byte[] bytes) {
        int accum = 0;
        for (int i = 0; i < 4; i++) {
            accum |= (bytes[i] & 0xff) << i * 8;
        }
        return Float.intBitsToFloat(accum);
    }

    public static byte[] floatToBytes(float value) {
        int fbit = Float.floatToIntBits(value);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));

        }
        int len = b.length;
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    public static byte[] charToBytes(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static char bytesToChar(byte[] bytes) {
        return (char) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
    }

}
