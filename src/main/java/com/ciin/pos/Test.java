package com.ciin.pos;

import com.ciin.pos.util.ByteUtils;

public class Test {

    public static void main(String[] args) {
//        Printer printer = new NetworkPrinter("192.168.10.60");
//        String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
//        Template template = new Template(read);
//        PrintTask printTask = new PrintTask(template);
//        printer.print(printTask);

        byte[] bytes = ByteUtils.intToByteArray(65535);
//        System.out.println(Arrays.toString(bytes));
        int i = ByteUtils.byteArrayToInt(bytes);
        System.out.println(i);

    }
}