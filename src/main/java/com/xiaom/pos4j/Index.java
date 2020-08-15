package com.xiaom.pos4j;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.parser.Document;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.FileUtils;

import java.io.File;

public class Index {

    public static void main(String[] args) {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        Template template = Template.compile(new File("example.xml"));
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Document document = template.toDocument(env);
//            Device device = DeviceFactory.device_58();
//            device.setOrderSet(CloudPrintOrderSet.INSTANCE);
//            device.setCharset(Charset.defaultCharset());
//            byte[] bytes = document.toBytes(device);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));


//        Size[] values = Size.values();
//        Map<String, Size> map = new HashMap<>();
//        for (Size value : values) {
//            map.put(value.name(), value);
//        }
//        long sTime = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++) {
//            Size size = Size.of("w1h1", Size.w1h1);
////            map.getOrDefault("w1h1", Size.w1h1);
//        }
//
//        long nTime = System.currentTimeMillis();
//        System.out.println(String.format("耗时: %sms", (nTime - sTime)));

//        System.out.println(document);
//        Device device = DeviceFactory.device_58();
//        device.setOrderSet(CloudPrintOrderSet.INSTANCE);
//        device.setCharset(Charset.defaultCharset());
//        byte[] bytes = document.toBytes(device);
//        String s = new String(bytes);
//        System.out.println(s);
    }
}
