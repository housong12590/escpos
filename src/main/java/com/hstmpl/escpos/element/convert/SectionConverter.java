package com.hstmpl.escpos.element.convert;


import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.element.Section;
import com.hstmpl.escpos.parser.StyleSheet;
import com.hstmpl.escpos.element.Text;
import com.hstmpl.escpos.orderset.OrderSet;
import com.hstmpl.escpos.parser.ElementKit;
import com.hstmpl.escpos.util.ByteBuffer;

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
