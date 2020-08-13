package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.util.IdUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Index {

    public static void main(String[] args) {
        String text = "订单编号: #{order_id}    下单时间: #{pay_time}   #{pay_time}";
        Map<String, Object> env = new HashMap<>();
        env.put("order_id", IdUtils.generateId());
        env.put("pay_time", "2020-08-13 01:10");

        File file = new File("D:\\work\\java\\escpos\\example.xml");
        Template template = Template.compile(file);
        System.out.println(template);
        Document document = template.toDocument(MapTransform.get(), env);
        System.out.println(document);
    }
}
