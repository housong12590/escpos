package com.hstmpl.escpos.parser;

import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.element.*;
import com.hstmpl.escpos.element.convert.*;
import com.hstmpl.escpos.element.generator.*;
import com.hstmpl.escpos.util.ClassUtils;
import com.hstmpl.escpos.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hous
 */
public class ElementKit {

    private static Map<Class<? extends Element>, Converter<? extends Element>> converterMap = new HashMap<>();
    private static Map<Class<? extends Element>, Generator<? extends Element>> generatorMap = new HashMap<>();
    private static Map<String, Class<? extends Element>> elementMap = new HashMap<>();

    static {
        register(Text.class, TextConverter.class, TextGenerator.class);
        register(Image.class, ImageConverter.class, ImageGenerator.class);
        register(Table.class, TableConverter.class, TableGenerator.class);
        register(Section.class, SectionConverter.class, SectionGenerator.class);
        register(Block.class, BlockConverter.class, BlockGenerator.class);
    }

    public static void register(Class<? extends Element> elementClazz,
                                Class<? extends Converter<? extends Element>> converterClazz,
                                Class<? extends Generator<? extends Element>> generatorClazz) {
        converterMap.put(elementClazz, newInstance(converterClazz));
        generatorMap.put(elementClazz, newInstance(generatorClazz));
        String elementName = elementClazz.getSimpleName().toLowerCase();
        elementMap.put(elementName, elementClazz);
    }

    public static <T extends Element> T getElement(ElementExample example, Transform transform, Object env) {
        Class<? extends Element> elementClass = example.getElementClass();
        Generator<? extends Element> generator = generatorMap.get(elementClass);
        return ClassUtils.cast(generator.create(example, transform, env));
    }

    public static Class<? extends Element> getElementClass(String elName) {
        return elementMap.get(elName);
    }

    public static byte[] matchConverterToBytes(Element element, StyleSheet styleSheet, Device device) {
        Converter<Element> converter = ClassUtils.cast(converterMap.get(element.getClass()));
        if (converter != null) {
            return converter.toBytes(device, styleSheet, element);
        }
        LogUtils.error(String.format("没有匹配到%s元素的转换器", element.getClass().getName()));
        LogUtils.info("可用的转换器如下: ");
        converterMap.forEach((key, value) -> LogUtils.info(value.getClass().getName()));
        return new byte[0];
    }

    private static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
