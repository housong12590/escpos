package com.xiaom.pos4j.orderset;

/**
 * @author hous
 */
public class OrderSetFactory {

    private static OrderSet orderSet = new StandardOrderSet();

    public static OrderSet getDefault() {
        return orderSet;
    }

}
