package com.cin.pos;

import com.cin.pos.common.Dict;
import com.cin.pos.element.Document;
import com.cin.pos.parser.PrintTemplate;
import com.cin.pos.printer.NetworkPrinter;
import com.cin.pos.util.FileUtils;
import com.cin.pos.util.StringUtils;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        String templateStr = FileUtils.fileRead("E:\\code\\java\\printer\\printer_client\\src\\main\\resources\\template\\品项核算单.xml");
        String dataStr = FileUtils.fileRead("E:\\code\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
        Dict data = Dict.create(dataStr);
        PrintTemplate template = new PrintTemplate(templateStr, data);
        Document document = template.toDocument();
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        printer.print(document);
//        String text = "他走后，每次看他朋友圈发西港的海景房别墅、菲佣、中国大厨做的饭菜，几个程序员同行们都非常羡慕，在微信群里各种讨论@他出来，吵着闹着要去柬埔寨工作，但这小子从没回复过。";
//        List<String> strings = StringUtils.splitOfGBKLength(text, 32, Align.left);
//        for (String string : strings) {
//            System.out.println(string);
//        }
    }


    private static Integer getxx(List<Integer> list, int number) {
        if (list.isEmpty()) {
            return null;
        }
        for (Integer integer : list) {
            if (integer >= number) {
                return integer;
            }
        }
        return 0;
    }

    public static void test01(int count, String value, int len) {
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            StringUtils.splitOfGBKLength(value, len);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("优化前 耗时: %sms", endTime - sTime));
    }

    public static void test02(int count, String value, int len) {
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            StringUtils.splitOfGBKLength(value, len);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("优化后 耗时: %sms", endTime - sTime));
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
