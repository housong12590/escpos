package com.cin.pos;

import com.cin.pos.element.exception.TemplateParseException;
import com.cin.pos.parser.Template;
import com.cin.pos.printer.NetworkPrinter;
import com.cin.pos.printer.PrintTask;
import com.cin.pos.util.FileUtils;

public class Test {

    public static void main(String[] args) throws TemplateParseException {
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        String fileRead = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\test_print.xml");
        Template template = new Template(fileRead);
        PrintTask printTask = new PrintTask("111",template);
        printer.print(printTask);
    }


    public static class Hero {
        private String name;

        private int age;

        public Hero() {
        }

        public Hero(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
