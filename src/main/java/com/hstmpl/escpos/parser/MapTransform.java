package com.hstmpl.escpos.parser;

import com.hstmpl.escpos.util.Expression;
import com.hstmpl.escpos.util.LRUCache;

import java.util.Map;

public class MapTransform implements Transform {


    private static final int CACHE_SIZE = 10000;

    private static MapTransform INSTANCE = new MapTransform();

    private LRUCache<String, Expression> expressionLRUCache = new LRUCache<>(CACHE_SIZE);

    public static MapTransform get() {
        return INSTANCE;
    }

    @Override
    public <T> T get(String key, Object env) {
        if (env instanceof Map) {
            return getExpression(key).getValue(env);
        }
        return null;
    }

    private Expression getExpression(String key) {
        Expression expression = expressionLRUCache.get(key);
        if (expression == null) {
            expression = Expression.of(key);
            expressionLRUCache.put(key, expression);
        }
        return expression;
    }
}
