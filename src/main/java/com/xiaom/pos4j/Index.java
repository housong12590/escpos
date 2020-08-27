package com.xiaom.pos4j;

import com.xiaom.pos4j.enums.Size;

public class Index {

    static int count = 100000;

    public static void main(String[] args) {
        test01();
    }

    public static void test01() {
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            Size size = Size.of("w1h3", Size.w1h1);
//            System.out.println(size);
        }
        long nTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %sms", (nTime - sTime)));
    }


}
