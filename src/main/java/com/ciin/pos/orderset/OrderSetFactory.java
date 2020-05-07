package com.ciin.pos.orderset;

public class OrderSetFactory {

    private static OrderSet orderSet = new StandardOrderSet();

    public static OrderSet getDefault() {
        return orderSet;
    }

}
