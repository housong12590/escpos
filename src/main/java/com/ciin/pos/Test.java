package com.ciin.pos;

import com.ciin.pos.element.exception.TemplateParseException;
import com.ciin.pos.printer.PrintTask;
import com.ciin.pos.util.Utils;
import com.ciin.pos.parser.Template;
import com.ciin.pos.printer.NetworkPrinter;
import com.ciin.pos.util.FileUtils;

public class Test {

    public static void main(String[] args) throws TemplateParseException {
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        String fileRead = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
        Template template = new Template(fileRead);
        PrintTask printTask = new PrintTask("111",template);
        printer.print(printTask);
        Utils.sleep(2000);
        printer.release();
    }
}
