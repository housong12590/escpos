package com.xiaom.pos4j;

import com.googlecode.aviator.AviatorEvaluator;
import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.parser.Document;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.Expression;
import com.xiaom.pos4j.util.FileUtils;

import java.io.File;

public class Index {

    static int count = 1000000;

    public static void main(String[] args) {
        test01();
    }

    public static void test01() {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        Template template = Template.compile(new File("example.xml"));
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 1_000_000; i++) {
            Document document = template.toDocument(env);
//            Device device = DeviceFactory.device_58();
//            device.setOrderSet(CloudPrintOrderSet.INSTANCE);
//            device.setCharset(Charset.defaultCharset());
//            byte[] bytes = document.toBytes(device);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
    }

    public static void test02() {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            AviatorEvaluator.execute("store_name", env, true);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
//         env.getValue("name");
    }


    public static void test03() {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Object o = env.getValue("store.user.attach[0].name");
//            System.out.println(o);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
//         env.getValue("name");
    }

    public static void test05() {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        Expression expression = Expression.of("store.user.attach[0].name");
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Object o = expression.getValue(env);
//            System.out.println(o);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
    }


}
