package com.xiaom.pos4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hous
 */
public class ClassUtils {

    public static boolean isPrimitive(Class<?> cls) {
        return isInt(cls)
                || isString(cls)
                || isLong(cls)
                || isChar(cls)
                || isShort(cls)
                || isDouble(cls)
                || isBool(cls)
                || isByte(cls)
                || isFloat(cls);
    }

    public static boolean isPrimitive(Object obj) {
        return isPrimitive(obj.getClass());
    }

    public static boolean isInt(Object obj) {
        return obj instanceof Integer;
    }

    public static boolean isInt(Class<?> cls) {
        return cls == int.class || cls == Integer.class;
    }

    public static boolean isString(Object obj) {
        return obj instanceof String;
    }

    public static boolean isString(Class<?> cls) {
        return cls == String.class;
    }

    public static boolean isBool(Object obj) {
        return obj instanceof Boolean;
    }

    public static boolean isBool(Class<?> cls) {
        return cls == boolean.class || cls == Boolean.class;
    }

    public static boolean isFloat(Object obj) {
        return obj instanceof Float;
    }

    public static boolean isFloat(Class<?> cls) {
        return cls == float.class || cls == Float.class;
    }

    public static boolean isDouble(Object obj) {
        return obj instanceof Double;
    }

    public static boolean isDouble(Class<?> cls) {
        return cls == double.class || cls == Double.class;
    }

    public static boolean isByte(Object obj) {
        return obj instanceof Byte;
    }

    public static boolean isByte(Class<?> cls) {
        return cls == byte.class || cls == Byte.class;
    }

    public static boolean isChar(Object obj) {
        return obj instanceof Character;
    }

    public static boolean isChar(Class<?> cls) {
        return cls == char.class || cls == Character.class;
    }

    public static boolean isShort(Object obj) {
        return obj instanceof Short;
    }

    public static boolean isShort(Class<?> cls) {
        return cls == short.class || cls == Short.class;
    }

    public static boolean isLong(Object obj) {
        return obj instanceof Long;
    }

    public static boolean isLong(Class<?> cls) {
        return cls == long.class || cls == Long.class;
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        if (obj == null) return null;
        return (T) obj;
    }

    public static Field[] getFields(Object bean) {
        Class<?> cls = bean.getClass();
        List<Field> _list = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            if (isStatic(field)) {
                continue;
            }
            _list.add(field);
        }
        return _list.toArray(new Field[0]);
    }

    public static Class<?> getParameterizedType(Field f) {
        Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型
        if (fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            Type[] types = pt.getActualTypeArguments();
            if (types != null && types.length > 0) {
                return (Class<?>) types[0];
            }
        }
        return null;
    }

}
