package com.xiaom.pos4j.element.convert;


import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.parser.StyleSheet;

/**
 * @author hous
 */
public interface Converter<T extends Element> {

    byte[] toBytes(Device device, StyleSheet styleSheet, T element);
}
