package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.util.FileUtils;

import java.io.File;

public class Index {

    public static void main(String[] args) {
        String text = "订单编号: #{order_id}    下单时间: #{pay_time}   #{pay_time}";
        String read = FileUtils.read("data.json");
        Dict env = Dict.create(read);

        File file = new File("example.xml");
        Template template = Template.compile(file);
        System.out.println(template);
        Document document = template.toDocument(MapTransform.get(), env);
        System.out.println(document);

    }
}
