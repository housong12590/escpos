package com.cin.pos.common;

import java.util.Arrays;

public class GBKString {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private int count;
    private char[] value;
    private int[] charLen;
    private int gbkLength;

    public GBKString() {
        this(16);
    }

    public GBKString(int capacity) {
        this.value = new char[capacity];
    }

    public GBKString(String str) {
        int len = str.length();
        this.value = new char[len + 16];
        this.charLen = new int[len + 16];
        append(str);
    }

    public GBKString append(String str) {
        char[] chars = str.toCharArray();
        int len = str.length();
        ensureCapacityInternal(count + len);
        System.arraycopy(chars, 0, value, count, chars.length);
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int charLength = c > 0xff ? 2 : 1;
            charLen[count + i] = charLength;
            gbkLength += charLength;
        }
        count += chars.length;
        return this;
    }


    public GBKString delete(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end > count)
            end = count;
        if (start > end)
            throw new StringIndexOutOfBoundsException();
        int len = end - start;
        if (len > 0) {
            for (int i = start; i < end; i++) {
                gbkLength -= charLen[i];
            }
            System.arraycopy(value, start + len, value, start, count - end);
            System.arraycopy(charLen, start + len, charLen, start, count - end);
            count -= len;
        }
        return this;
    }

    public char charAt(int index) {
        if (index >= value.length) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    public int charLength(int index) {
        if (index >= charLen.length) {
            throw new IndexOutOfBoundsException("");
        }
        return charLen[index];
    }

    public int length() {
        return count;
    }

    public boolean com() {
        return value.length == charLen.length;
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

    public int gbkLength() {
        return gbkLength;
    }

    public String toString() {
        return new String(value, 0, count);
    }
}
