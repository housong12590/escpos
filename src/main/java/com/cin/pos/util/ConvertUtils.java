package com.cin.pos.util;


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

    public static float toFloat(Object obj) {
        return toFloat(obj, 0.0f);
    }

    public static float toFloat(Object obj, float defValue) {
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

    public static double toDouble(Object obj) {
        return toDouble(obj, 0.00d);
    }

    public static double toDouble(Object obj, double defValue) {
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

    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static int toInt(Object obj, int defValue) {
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

    public static boolean toBool(Object obj, boolean defValue) {
        if (obj == null) {
            return defValue;
        }
        return Boolean.parseBoolean(toString(obj));
    }
}
