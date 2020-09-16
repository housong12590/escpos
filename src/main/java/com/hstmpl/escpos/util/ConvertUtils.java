package com.hstmpl.escpos.util;


/**
 * @author hous
 */
public class ConvertUtils {

    public static String toString(Object obj) {
        return toString(obj, null);
    }

    public static String toString(Object obj, String defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    public static Float toFloat(Object obj) {
        return toFloat(obj, 0.0f);
    }

    public static Float toFloat(Object obj, float defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        try {
            return Float.parseFloat(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static Double toDouble(Object obj) {
        return toDouble(obj, 0.00d);
    }

    public static Double toDouble(Object obj, double defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        try {
            return Double.parseDouble(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static Integer toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static Integer toInt(Object obj, int defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Integer) {
            return (int) obj;
        }
        try {
            return Integer.parseInt(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static Boolean toBool(Object obj) {
        return toBool(obj, false);
    }

    public static Boolean toBool(Object obj, boolean defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Boolean) {
            return (boolean) obj;
        }
        return Boolean.parseBoolean(toString(obj));
    }

    public static Byte toByte(Object obj) {
        return toByte(obj, (byte) 0);
    }

    public static Byte toByte(Object obj, byte defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Byte) {
            return (byte) obj;
        }
        try {
            return Byte.parseByte(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static Short toShort(Object obj) {
        return toShort(obj, (short) 0);
    }

    public static Short toShort(Object obj, Short defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Short) {
            return (Short) obj;
        }
        try {
            return Short.parseShort(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static Long toLong(Object obj) {
        return toLong(obj, 0L);
    }

    public static Long toLong(Object obj, Long defValue) {
        if (obj == null) {
            return defValue;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        try {
            return Long.parseLong(toString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @SuppressWarnings("unchecked")
    public static <T> T matchTypeCast(Class<T> type, Object obj) {
        if (ClassUtils.isString(type)) {
            return (T) ConvertUtils.toString(obj);
        } else if (ClassUtils.isInt(type)) {
            return (T) ConvertUtils.toInt(obj);
        } else if (ClassUtils.isLong(type)) {
            return (T) ConvertUtils.toLong(obj);
        } else if (ClassUtils.isFloat(type)) {
            return (T) ConvertUtils.toFloat(obj);
        } else if (ClassUtils.isDouble(type)) {
            return (T) ConvertUtils.toDouble(obj);
        } else if (ClassUtils.isBool(type)) {
            return (T) ConvertUtils.toBool(obj);
        } else if (ClassUtils.isShort(type)) {
            return (T) ConvertUtils.toShort(obj);
        } else if (ClassUtils.isByte(type)) {
            return (T) ConvertUtils.toByte(obj);
        }
        return null;
    }
}
