package com.hstmpl.escpos.util;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author hous
 */
public class PropertiesUtils {
    //通过传入的路径及key，获得对应的值
    public static String getValue(String path, String key) {
        Properties properties = getProperties(path);
        return properties.getProperty(key);
    }

    public static Properties getProperties(String path) {
        // 先从jar包外开始寻找配置文件 ,如果不存在再使用默认的配置文件
        Properties properties = new Properties();
        try {
            properties.load(new BufferedInputStream(new FileInputStream(path)));
        } catch (IOException e) {
            try {
                ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
                InputStream stream = classLoader.getResourceAsStream(path);
                properties.load(stream);
            } catch (Exception ignored) {
            }
        }
        return properties;
    }

    public static Map<String, Object> getPropertiesMap(String path) {
        Properties properties = getProperties(path);
        Map<String, Object> map = new HashMap<>();
        for (String propertyName : properties.stringPropertyNames()) {
            map.put(propertyName, properties.get(propertyName));
        }
        return map;
    }

    public static void mapperToObject(String path, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> propertiesMap = getPropertiesMap(path);
        for (Field field : fields) {
            field.setAccessible(true);
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object value = ignoreCaseMatch(field.getName(), propertiesMap);
                    if (value != null) {
                        value = ConvertUtils.matchTypeCast(field.getType(), value);
                        field.set(obj, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void mapperToFile(String path, Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        Properties properties = getProperties(path);
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                Object val = field.get(object);
                if (val != null) {
                    properties.setProperty(field.getName(), ConvertUtils.toString(val));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            properties.store(fos, "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(fos);
        }
    }

    private static Object ignoreCaseMatch(String key, Map<String, Object> map) {
        String ignoreStr = "_";
        key = key.replaceAll(ignoreStr, "");
        for (String originKey : map.keySet()) {
            String newKey = originKey.replaceAll(ignoreStr, "");
            if (key.equalsIgnoreCase(newKey)) {
                return map.get(originKey);
            }
        }
        return null;
    }
}
