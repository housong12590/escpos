package com.xiaom.pos4j;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.device.DeviceFactory;
import com.xiaom.pos4j.orderset.CloudPrintOrderSet;
import com.xiaom.pos4j.parser.Document;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

public class Index {

    static int count = 100000;

    public static void main(String[] args) {
    }

    public static void test01() {
        String data = FileUtils.read("data.json");
        Dict env = Dict.of(data);
        Template template = Template.compile(new File("example.xml"));
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Document document = template.toDocument(env);
            Device device = DeviceFactory.device_58();
            device.setOrderSet(CloudPrintOrderSet.INSTANCE);
            device.setCharset(Charset.defaultCharset());
            byte[] bytes = document.toBytes(device);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
    }


}
