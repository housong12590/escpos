package com.xiaom.pos4j.convert;

import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.element.*;
import com.xiaom.pos4j.util.LogUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hous
 */
public class ConverterKit {

    private static Map<Class<? extends Element>, Converter<Element>> converterMap = new HashMap<>();
    private static Map<String, Class<? extends Element>> elementMap = new HashMap<>();

    static {
        registerConverter(Text.class, new TextConverter());
        registerConverter(Image.class, new ImageConverter());
        registerConverter(Table.class, new TableConverter());
        registerConverter(Section.class, new SectionConverter());
        registerConverter(Group.class, new GroupConverter());
    }

    public static void registerConverter(Class<? extends Element> element, @SuppressWarnings("rawtypes") Converter converter) {
        converterMap.put(element, converter);
        String elementName = element.getSimpleName().toLowerCase();
        elementMap.put(elementName, element);


    }

    public static Converter<? extends Element> matchConverter(String elementName) {
        if (StringUtils.isNotEmpty(elementName)) {
            Class<? extends Element> elementClass = elementMap.get(elementName);
            if (elementClass != null) {
                return converterMap.get(elementClass);
            }
        }
        return null;
    }


    public static Element newElement(String elementName) {
        Class<? extends Element> aClass = elementMap.get(elementName);
        if (aClass != null) {
            try {
                return aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends Element> byte[] matchConverterToBytes(Element element, Device device) {
        Converter<Element> converter = converterMap.get(element.getClass());
        if (converter != null) {
            return converter.toBytes(device, element);
        }
        LogUtils.error(String.format("没有匹配到%s元素的转换器", element.getClass().getName()));
        LogUtils.info("可用的转换器如下: ");
        for (Map.Entry<Class<? extends Element>, Converter<Element>> entry : converterMap.entrySet()) {
            Converter<Element> value = entry.getValue();
            LogUtils.info(value.getClass().getName());
        }
        return new byte[0];
    }
}
