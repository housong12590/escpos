package com.hstmpl.escpos.element.convert;


import com.hstmpl.escpos.element.Element;
import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.parser.StyleSheet;

/**
 * @author hous
 */
public interface Converter<T extends Element> {

    byte[] toBytes(Device device, StyleSheet styleSheet, T element);
}
