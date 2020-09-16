package com.hstmpl.escpos.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author hous
 */
public class IOUtils {

    public static void safeClose(Closeable... closeables) {
        for (Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
