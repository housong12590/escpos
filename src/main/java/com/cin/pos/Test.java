package com.cin.pos;

import com.cin.pos.common.GBKString;

public class Test {

    public static void main(String[] args) {
//        String templateStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\结帐单.xml");
//        String dataStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
//        Dict data = Dict.create(dataStr);
//        PrintTemplate template = new PrintTemplate(templateStr, data);
//        Document document = template.toDocument();
//        System.out.println(document);

//        new StringBuilder().delete()

        String value = "1工1工工11 1中。)。";
        GBKString gbkString = new GBKString(value);
        String s = gbkString.toString();
        System.out.println(s);
        System.out.println(gbkString.length());
        gbkString.append("侯松");
        System.out.println(gbkString.length);
        System.out.println(gbkString.length);
        gbkString.delete(1, 3);
        System.out.println(gbkString.length);
        System.out.println(gbkString.toString());
    }

    public static int getGBKLength(String text) {
        char[] chars = text.toCharArray();
        int count = 0;
        for (char c : chars) {
            count += c > 0xff ? 2 : 1;
        }
//        try {
//            count = text.getBytes("gbk").length;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return count;
    }
}
