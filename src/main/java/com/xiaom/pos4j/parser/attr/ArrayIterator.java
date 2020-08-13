package com.xiaom.pos4j.parser.attr;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T> {

    private T[] arr;
    private int index;
    private int endIndex;

    public ArrayIterator(T[] arr) {
        this(arr, 0);
    }

    public ArrayIterator(T[] arr, int startIndex) {
        this.arr = arr;
        this.index = startIndex;
        this.endIndex = arr.length;
    }

    @Override
    public boolean hasNext() {
        return index < endIndex;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return arr[index++];
    }
}
