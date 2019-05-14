package com.cin.pos;

import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.printer.NetworkPrinter;
import com.cin.pos.util.JSONUtil;
import com.cin.pos.util.Util;

import java.io.File;
import java.util.Map;

class Index {
    public static void main(String[] args) {
        test02();
    }

    public static void test01() {
        String templateStr = Util.readString(new File("D:\\work\\java\\escpos4j\\src\\main\\resources\\template\\结帐单.xml"));
        String dataJson = Util.readString(new File("D:\\work\\java\\escpos4j\\src\\main\\resources\\template\\data.json"));
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        Map map = JSONUtil.toMap(dataJson);
        printer.print(templateStr, map);
        printer.setOnPrintCallback(new OnPrintCallback() {
            @Override
            public void onSuccess(Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(Object o, Throwable throwable) {
                System.out.println(o + " " + throwable.getMessage());
            }
        });
    }

    public static void test02() {
        long sTime = System.currentTimeMillis();
        for (long i = 0; i < 1000000000; i++) {
//            Converter table = ConverterKit.matchConverter("table");
//            Class cls = Text.class;
        }
        long diffTime = System.currentTimeMillis();
        System.out.println("耗时: " + (diffTime - sTime) + "ms");
    }
}
