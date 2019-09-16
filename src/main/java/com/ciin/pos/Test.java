package com.ciin.pos;

import com.ciin.pos.parser.Template;
import com.ciin.pos.printer.NetworkPrinter;
import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.printer.Printer;
import com.ciin.pos.util.FileUtils;

public class Test {

    public static void main(String[] args) {
        Printer printer = new NetworkPrinter("192.168.10.60");
        String read = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
        Template template = new Template(read);
        PrintTask printTask = new PrintTask(template);
        printer.print(printTask);
    }
}