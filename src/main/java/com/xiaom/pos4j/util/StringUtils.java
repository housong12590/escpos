package com.xiaom.pos4j.util;


import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author hous
 */
public class StringUtils {

    public static int lengthOfGBK(String value) {
        if (StringUtils.isEmpty(value)) {
            return 0;
        }
        int length = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            length += lengthOfGBK(c);
        }
        return length;
    }

    public static int lengthOfGBK(char c) {
        return c > 0xff ? 2 : 1;
    }

    public static List<String> splitOfGBKLength(String text, int len) {
        return splitOfGBKLength(text, len, null, Size.w1h1);
    }

    public static List<String> splitOfGBKLength(String text, int len, Align align, Size size) {
        char[] chars = text.toCharArray();
        int subLen = 0;
        List<String> _list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            int charLen = lengthOfGBK(c) * size.w;
            if (subLen + charLen > len) {
                subLen = charLen;
                _list.add(sb.toString());
                sb.delete(0, sb.length());
                sb.append(c);
            } else {
                sb.append(c);
                subLen += charLen;
            }
        }
        if (sb.length() != 0) {
            String value = "";
            if (align != null) {
                switch (align) {
                    case left:
                        value = fillBlankRight2GBKLength(sb.toString(), len, size);
                        break;
                    case right:
                        value = fillBlankLeft2GBKLength(sb.toString(), len, size);
                        break;
                    case center:
                        value = fillBlankBoth2GBKLength(sb.toString(), len, size);
                        break;
                }
            } else {
                value = sb.toString();
            }
            _list.add(value);
        }
        return _list;
    }

    public static String fillBlankLeft2GBKLength(String src, int dstLen, Size size) {
        int srcLen = lengthOfGBK(src) * size.w;
        int len = dstLen - srcLen;
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len; i++) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }

    public static String fillBlankRight2GBKLength(String src, int dstLen, Size size) {
        int srcLen = lengthOfGBK(src) * size.w;
        int len = dstLen - srcLen;
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String fillBlankBoth2GBKLength(String src, int dstLen, Size size) {
        int srcLen = lengthOfGBK(src) * size.w;
        int len = dstLen - srcLen;
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len; i++) {
            if (i % 2 == 0) {
                sb.insert(0, " ");
            } else {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String emptyLine(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static String inputStream2String(InputStream inputStream) {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        try {
            for (int n; (n = inputStream.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(inputStream);
        }
        return null;
    }

    public static InputStream string2inputStream(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String[] spiltString(String text, int subLen) {
        int spiltNum = text.length() / subLen + (text.length() % subLen == 0 ? 0 : 1);
        String[] subs = new String[spiltNum];
        int index = 0;
        for (int i = 0; i < text.length(); i = i + subLen) {
            int position = i + subLen;
            if (position > text.length()) {
                position = text.length();
            }
            subs[index] = text.substring(i, position);
            index++;
        }
        return subs;
    }

    public static String toString(Object obj) {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(key).append("=").append(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj.getClass().getSimpleName() + " [" + sb.toString() + "]";
    }


    public static String arrayToString(Collection collection, String separator) {
        Iterator it = collection.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Object val = it.next();
            if (sb.length() == 0) {
                sb.append(val);
            } else {
                sb.append(separator).append(val);
            }
        }
        return sb.toString();
    }

    public static String arrayToString(Collection collection) {
        return arrayToString(collection, ",");
    }

    public static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }

    public static Map<String, Object> parseQueryString(String queryString) {
        Map<String, Object> param = new HashMap<>();
        if (isEmpty(queryString)) {
            return param;
        }
        for (String queryStr : queryString.split("&")) {
            String[] split = queryStr.split("=");
            if (split.length == 2) {
                param.put(split[0], split[1]);
            }
        }
        return param;
    }

    public static String toQueryString(Map<String, Object> queryMap) {
        StringBuilder sb = new StringBuilder();
        queryMap.forEach((s, object) -> sb.append(s).append("=").append(object).append("&"));
        return sb.substring(0, sb.length() - 1);
    }
}
