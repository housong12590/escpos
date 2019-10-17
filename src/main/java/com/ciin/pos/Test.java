package com.ciin.pos;


import com.ciin.pos.device.Device;
import com.ciin.pos.printer.ForwardingPrinter;

public class Test {

    public static void main(String[] args) {
        test01();
    }

    private static void test01() {
        ForwardingPrinter printer = new ForwardingPrinter(Device.getDefault(), "127.0.0.1",
                "驱动/GP-C80300 Series");
        printer.testPrint();
    }
}