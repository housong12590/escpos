package com.hstmpl.escpos.element.convert;

import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.element.Block;
import com.hstmpl.escpos.element.Element;
import com.hstmpl.escpos.parser.ElementKit;
import com.hstmpl.escpos.parser.StyleSheet;
import com.hstmpl.escpos.util.ByteBuffer;

/**
 * @author hous
 */
public class BlockConverter implements Converter<Block> {

    @Override
    public byte[] toBytes(Device device, StyleSheet styleSheet, Block element) {
        ByteBuffer buffer = new ByteBuffer();
        for (Element child : element.getChildren()) {
            byte[] bytes = ElementKit.matchConverterToBytes(child, styleSheet, device);
            buffer.write(bytes);
        }
        return buffer.toByteArray();
    }
}
