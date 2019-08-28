package com.cin.pos.util;

import java.io.Closeable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Utils {

    private static DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DateFormat getDefaultFormat() {
        return DEFAULT_FORMAT;
    }


    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void safeClose(Closeable... closeable) {
        for (Closeable c : closeable) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
