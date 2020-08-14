package com.xiaom.pos4j;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.parser.Template;
import com.xiaom.pos4j.util.FileUtils;

import java.io.File;

public class Index {

    public static void main(String[] args) {
        String data = FileUtils.read("data.json");
        Dict env = Dict.create(data);
        Template template = Template.compile(new File("example.xml"));
        Document document = template.toDocument(env);
        System.out.println(document);
    }


}
