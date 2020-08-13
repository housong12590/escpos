package com.xiaom.pos4j;


import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.device.Paper_58;
import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.orderset.CloudPrintOrderSet;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.FileUtils;

import java.nio.charset.Charset;

public class Index {

    public static void main(String[] args) {
        String content = FileUtils.read("外卖单.xml");
        String data = FileUtils.read("data.json");
        Template template = new Template(content, Dict.create(data));
        try {
            Device device = new Device(new Paper_58());
            device.setCharset(Charset.defaultCharset());
            device.setOrderSet(new CloudPrintOrderSet());

            long sTime = System.currentTimeMillis();
            Document document = null;
            byte[] bytes = null;
            for (int i = 0; i < 10000; i++) {
                document = template.toDocument();
            }
            bytes = document.toBytes(device);
            long nTime = System.currentTimeMillis();
            System.out.println(String.format("耗时: %sms", (nTime - sTime)));
            String s = new String(bytes);
            System.out.println(s);
        } catch (TemplateParseException e) {
            e.printStackTrace();
        }
    }
}
