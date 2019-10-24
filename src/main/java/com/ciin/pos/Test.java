package com.ciin.pos;


import com.ciin.pos.printer.NetworkPrinter;

public class Test {

    public static void main(String[] args) {
        test02();
    }

    private static void test01() {


    }

    private static void test02() {
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        for (int i = 0; i < 100; i++) {
            printer.syncTestPrint();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}