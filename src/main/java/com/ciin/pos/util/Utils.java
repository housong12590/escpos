package com.ciin.pos.util;

import java.io.Closeable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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


    public static List<byte[]> splitArray(byte[] bytes, int len) {
        List<byte[]> _list = new ArrayList<>();
        int position = 0;
        if (bytes.length > len) {
            int count = bytes.length / len + (bytes.length % len == 0 ? 0 : 1);
            for (int i = 0; i < count; i++) {
                if (bytes.length - position < len) {
                    len = bytes.length - position;
                }
                byte[] new_arr = new byte[len];
                System.arraycopy(bytes, position, new_arr, 0, len);
                _list.add(new_arr);
                position += len;
            }
        } else {
            _list.add(bytes);
        }
        return _list;
    }
}
