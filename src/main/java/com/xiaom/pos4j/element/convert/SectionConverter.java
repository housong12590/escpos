package com.xiaom.pos4j.element.convert;


import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.element.Section;
import com.xiaom.pos4j.parser.StyleSheet;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.orderset.OrderSet;
import com.xiaom.pos4j.parser.ElementKit;
import com.xiaom.pos4j.util.ByteBuffer;

/**
 * @author hous
 */
public class SectionConverter implements Converter<Section> {

    @Override
    public byte[] toBytes(Device device, StyleSheet styleSheet, Section section) {
        OrderSet orderSet = device.getOrderSet();
        ByteBuffer buffer = new ByteBuffer();
        int marginTop = section.getMarginTop();
        if (marginTop > 0) {
            buffer.write(orderSet.paperFeed(marginTop));
        }
        for (Text text : section.getTexts()) {
            byte[] bytes = ElementKit.matchConverterToBytes(text,styleSheet ,device);
            buffer.write(bytes);
        }
        buffer.write(orderSet.newLine());
        int marginBottom = section.getMarginBottom();
        if (marginBottom > 0) {
            buffer.write(orderSet.paperFeed(marginBottom));
        }
        return buffer.toByteArray();
    }
}
