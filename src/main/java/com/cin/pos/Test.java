package com.cin.pos;

import com.cin.pos.callback.OnPrintCallback;
import com.cin.pos.common.Dict;
import com.cin.pos.parser.Template;
import com.cin.pos.printer.NetworkPrinter;
import com.cin.pos.printer.PrintTask;
import com.cin.pos.printer.Printer;
import com.cin.pos.util.FileUtils;
import com.cin.pos.util.StringUtils;
import com.cin.pos.util.Utils;

import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        String templateStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\结帐单.xml");
        String dataStr = FileUtils.fileRead("D:\\work\\java\\printer\\printer_client\\src\\main\\resources\\template\\data.json");
        Dict data = Dict.create(dataStr);
        Template template = new Template(templateStr, data);
        NetworkPrinter printer = new NetworkPrinter("192.168.10.60");
        printer.setPrintCallback(new OnPrintCallback() {
            @Override
            public void onSuccess(Printer printer, PrintTask printTask) {

            }

            @Override
            public void onError(Printer printer, PrintTask printTask, String errorMsg) {

            }

            @Override
            public void onCancel(Printer printer, PrintTask printTask) {

            }

            @Override
            public void onConnectError(Printer printer) {
                System.out.println("打印连接失败");
            }
        });
        for (int i = 0; i < 10; i++) {
            PrintTask printTask = new PrintTask(template);
            printer.print(printTask);
            Utils.sleep(10 * 60 * 1000);
        }
        printer.release();
    }

    private static void test04() {
        long sTime = System.currentTimeMillis();
        long temp = 0;
        for (long i = 0; i < 10_000_000_000L; i++) {
            try {
                temp = i;
            } catch (Exception e) {

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", endTime - sTime));
    }

    private static void test03() {
        long sTime = System.currentTimeMillis();
        long temp = 0;
        for (long i = 0; i < 10_000_000_000L; i++) {
            temp = i;
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", endTime - sTime));
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
