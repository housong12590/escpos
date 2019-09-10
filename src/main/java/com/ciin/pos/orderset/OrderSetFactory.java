package com.ciin.pos.orderset;

public class OrderSetFactory {

    private static OrderSet orderSet = new GprinterOrderSet();

    public static OrderSet getDefault() {
        return orderSet;
    }

}
