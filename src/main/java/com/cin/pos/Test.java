package com.cin.pos;

import com.cin.pos.element.Document;
import com.cin.pos.parser.PrintTemplate;
import com.cin.pos.util.FileUtils;
import com.cin.pos.util.JSONUtils;

import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String templateStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\结帐单.xml");
        String dataStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
        Map data = JSONUtils.toMap(dataStr);
        PrintTemplate template = new PrintTemplate(templateStr, data);
        Document document = template.toDocument();
        System.out.println(document);

    }
}
