package com.cin.pos.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static int lengthOfGBK1(String value) {
        if (value == null)
            return 0;
        StringBuilder buff = new StringBuilder(value);
        int length = 0;
        String stmp;
        for (int i = 0; i < buff.length(); i++) {
            stmp = buff.substring(i, i + 1);
            try {
                stmp = new String(stmp.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stmp.getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }

    public static int lengthOfGBK(String value) {
        if (isEmpty(value)) {
            return 0;
        } else {
            int length = 0;

            for (int i = 0; i < value.length(); ++i) {
                char c = value.charAt(i);
                length += lengthOfGBK(c);
            }

            return length;
        }
    }

    public static int lengthOfGBK(char c) {
        return c > 255 ? 2 : 1;
    }

    public static List<String> splitStringLenOfGBK(String text, int len) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int tempLen = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            tempLen += lengthOfGBK(c);
            if (tempLen >= len) {
                list.add(sb.toString());
                tempLen = 0;
                sb.delete(0, sb.length() - 1);
            }
            sb.append(c);
        }
        if (sb.length() != 0) {
            list.add(sb.toString());
        }
        return list;
    }

    private static String subStringOfGBK(String text, int start, int len) {
        int end = start + len / 2;
        if (end >= text.length()) {
            return text.substring(start);
        }
        String s1 = text.substring(start, end);
        int s1Len = lengthOfGBK(s1);
        int diffLen = len - s1Len;
        if (diffLen == 0) {
            return s1;
        } else if (diffLen <= 2) {
            String s2 = text.substring(end, end + 1);
            int s2Len = lengthOfGBK(s2);
            if (s2Len < diffLen) {
                String s3 = text.substring(end + 1, end + 2);
                int s3Len = lengthOfGBK(s3);
                if (s3Len + s2Len == diffLen) {
                    return s1 + s2 + s3;
                } else {
                    return s1 + s2;
                }
            } else if (s2Len == diffLen) {
                return s1 + s2;
            } else {
                return s1;
            }
        } else {
            return s1 + subStringOfGBK(text, end, diffLen);
        }
    }

    public static String fillBlankLeft2GBKLength(String src, int dstLen) {
        int srcLen = lengthOfGBK(src);
        int len = dstLen - srcLen;
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len; i++) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }

    public static String fillBlankRight2GBKLength(String src, int dstLen) {
        int srcLen = lengthOfGBK(src);
        int len = dstLen - srcLen;
        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String fillBlankBoth2GBKLength(String src, int dstLen) {
        int srcLen = lengthOfGBK(src);
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

    public static String subExpression(Map map, Pattern regexp, String text) {
        return subExpression(map, regexp, text, null);
    }


    public static String subExpression(Map map, Pattern regexp, String text, String defValue) {
        Matcher matcher = regexp.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = getValue(map, key);
            if (value == null) {
                if (defValue != null) {
                    value = defValue;
                } else {
                    value = "null";
                }
            }
            String valueStr = value.toString();
            matcher.appendReplacement(sb, valueStr);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean isEmpty(String text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String text) {
        if (text != null && text.length() != 0) {
            return true;
        }
        return false;
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
            Util.ioClose(inputStream);
        }
        return null;
    }

    public static InputStream string2inputStream(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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

    public static String getExpressionKey(Pattern pattern, String expression) {
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
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


    public static Object getValue(Map map, String key) {
        if (map == null || isEmpty(key)) {
            return null;
        }
        String[] split = key.split("\\.");
        LinkedList<String> keys = new LinkedList<>(Arrays.asList(split));
        if (!keys.isEmpty()) {
            key = keys.pop();
            key = key.trim();
        }
        Object obj = map.get(key);
        if (keys.isEmpty()) {
            return obj;
        }
        key = StringUtil.arrayToString(keys, ".");
        try {
            map = (Map) obj;
        } catch (Exception e) {
            return null;
        }
        return getValue(map, key);
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
}
