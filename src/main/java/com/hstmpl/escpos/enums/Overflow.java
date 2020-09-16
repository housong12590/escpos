package com.hstmpl.escpos.enums;

public enum Overflow {

    hidden,

    newline;


    public static Overflow of(String value, Overflow defValue) {
        for (Overflow overflow : Overflow.values()) {
            if (value.equals(overflow.name())) {
                return overflow;
            }
        }
        return defValue;
    }
}
