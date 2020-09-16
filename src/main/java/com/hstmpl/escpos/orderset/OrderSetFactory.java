package com.hstmpl.escpos.orderset;

/**
 * @author hous
 */
public class OrderSetFactory {


    public static OrderSet getDefault() {
        return StandardOrderSet.INSTANCE;
    }

    public static OrderSet cloudPrint() {
        return CloudPrintOrderSet.INSTANCE;
    }

}
