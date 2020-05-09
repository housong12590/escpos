package com.ciin.pos.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int value, TimeUnit unit) {
        try {
            unit.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
