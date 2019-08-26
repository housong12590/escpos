package com.cin.pos.common;

import java.util.Arrays;

public class GBKString {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    public int length;
    private char[] value;
    private int[] charLen;
    private int index;

    public GBKString() {
        this(16);
    }

    public GBKString(int capacity) {
        this.value = new char[capacity];
    }

    public GBKString(String str) {
        this.value = new char[str.length() + 16];
        this.charLen = new int[str.length() + 16];
        append(str);
    }

    public GBKString append(String str) {
        char[] chars = str.toCharArray();
        ensureCapacityInternal(value.length + str.length());
        for (char aChar : chars) {
            value[index] = aChar;
            int charLength = getGBKCharLength(aChar);
            charLen[index] = charLength;
            length += charLength;
            index++;
        }
        return this;
    }

    public GBKString delete(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > value.length)
            end = value.length;
        if (start > end)
            throw new StringIndexOutOfBoundsException();
        int len = end - start;
        if (len > 0) {
            for (int i = start; i <= end; i++) {
                length -= charLen[i];
            }
            System.arraycopy(value, start + len, value, start, value.length - end);
            System.arraycopy(charLen, start + len, charLen, start, charLen.length - end);
        }
        return this;
    }

    public int length() {
        return length;
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        if (minimumCapacity - value.length > 0) {
            int newCapacity = newCapacity(minimumCapacity);
            value = Arrays.copyOf(value, newCapacity);
            charLen = Arrays.copyOf(charLen, newCapacity);
        }
    }

    private int newCapacity(int minCapacity) {
        int newCapacity = (value.length << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0) ? hugeCapacity(minCapacity) : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? minCapacity : MAX_ARRAY_SIZE;
    }

    private int getGBKCharLength(char c) {
        return c > 0xff ? 2 : 1;
    }

    public String toString() {
        return new String(value, 0, value.length);
    }
}
