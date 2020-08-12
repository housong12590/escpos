package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.util.IdUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Index {

    public static void main(String[] args) {
        String text = "订单编号: #{order_id}    下单时间: #{pay_time}   #{pay_time}";
        Map<String, Object> env = new HashMap<>();
        env.put("order_id", IdUtils.generateId());
        env.put("pay_time", "2020-08-13 01:10");

        Matcher matcher = Const.PARSE_PATTERN.matcher(text);
        List<Placeholder> list = new ArrayList<>();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String value = matcher.group(1);
            Placeholder placeholder = new Placeholder(start, end, value);
            System.out.println(placeholder);
            list.add(placeholder);
        }
        StringBuilder sb = new StringBuilder(text);
        for (int i = list.size() - 1; i >= 0; i--) {
            Placeholder p = list.get(i);
            String x = p.getVariable().execute(MapTransform.get(),env);
            sb.replace(p.getStart(), p.getEnd(), x);
            
        }

        System.out.println(sb.toString());
    }
}
