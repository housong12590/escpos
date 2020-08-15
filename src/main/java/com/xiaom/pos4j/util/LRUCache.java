package com.xiaom.pos4j.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int MAX_SIZE;

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }

    public LRUCache(int cacheSize) {
        super(16, 0.75f, true);
        this.MAX_SIZE = cacheSize;
    }
}