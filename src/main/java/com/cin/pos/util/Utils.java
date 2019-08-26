package com.cin.pos.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {

    private static DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DateFormat getDefaultFormat() {
        return DEFAULT_FORMAT;
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

    public static InputStream stringToInputStream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String fileRead(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(reader);
        }
        return null;
    }


}
