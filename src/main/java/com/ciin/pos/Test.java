package com.ciin.pos;

import com.ciin.pos.common.Dict;
import com.ciin.pos.parser.Template;
import com.ciin.pos.printer.NetworkPrinter;
import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;
import com.ciin.pos.util.FileUtils;

public class Test {

    public static void main(String[] args) throws InterruptedException {

//        Printer printer = new DrivePrinter(new Device(new Paper_80()), "\\\\DESKTOP-GSJUGH8\\GP-C80300 Series");
        Printer printer = new NetworkPrinter("192.168.10.60");
        for (int i = 0; i < 2; i++) {
            String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\结帐单.xml");
            String data = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
            Template template = new Template(read, Dict.create(data));
            PrintTask printTask = new PrintTask(template);
            printer.print(printTask);
        }

        Thread.sleep(30000);

        for (int i = 0; i < 2; i++) {
            String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\结帐单.xml");
            String data = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
            Template template = new Template(read, Dict.create(data));
            PrintTask printTask = new PrintTask(template);
            printer.print(printTask);
        }
//        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//        for (PrintService service : printServices) {
//            System.out.println(service.getName());
//        }
    }
}