package com.hstmpl.escpos.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author hous
 */
public class Base64Utils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static byte[] encode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    public static String encodeToString(String src) {
        byte[] bytes = encode(src.getBytes(DEFAULT_CHARSET));
        return new String(bytes, DEFAULT_CHARSET);
    }

    public static String decodeFromString(String src) {
        byte[] bytes = decode(src.getBytes(DEFAULT_CHARSET));
        return new String(bytes, DEFAULT_CHARSET);
    }

}
