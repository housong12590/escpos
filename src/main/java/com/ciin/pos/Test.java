package com.ciin.pos;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class Test {

    public static void main(String[] args) {
//        Printer printer = new NetworkPrinter("192.168.10.60");
//        String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
//        Template template = new Template(read);
//        PrintTask printTask = new PrintTask(template);
//        printer.print(printTask);

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());
        }
    }
}