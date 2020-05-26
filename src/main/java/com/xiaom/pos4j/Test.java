package com.xiaom.pos4j;


import com.xiaom.pos4j.common.Dict;
import com.xiaom.pos4j.element.*;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.FileUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Test {

    public static Map<String, Supplier<? extends Element>> map = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String templateStr = FileUtils.read("D:\\work\\java\\printer\\print_client\\src\\main\\resources\\template\\外卖单.xml");
        String dataJson = FileUtils.read("D:\\work\\java\\printer\\print_client\\src\\main\\resources\\template\\data.json");

//        long sTime = System.nanoTime();
        Template template = new Template(templateStr, Dict.create(dataJson));
        int count = 100000000;
        Document document = template.toDocument();

        map.put("image", Image::new);
        map.put("text", Text::new);
        map.put("table", Table::new);
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
//            document.toBytes(Device.getDefault());
//            Element image = map.get("image").get();
//            new Image();
            Image.class.newInstance();
        }

        long nTime = System.currentTimeMillis();
//        long nTime = System.nanoTime();
        System.out.println(nTime);
        long diff = nTime - sTime;
        System.out.println(String.format("耗时: %sms 平均耗时: %sms", diff, (double) diff / count));

    }
}