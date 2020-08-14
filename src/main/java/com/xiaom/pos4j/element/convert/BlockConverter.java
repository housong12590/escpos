package com.xiaom.pos4j.element.convert;

import com.xiaom.pos4j.element.Block;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.parser.ElementKit;
import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.device.Device;

/**
 * @author hous
 */
public class BlockConverter implements Converter<Block> {

    @Override
    public byte[] toBytes(Device device, Block element) {
        ByteBuffer buffer = new ByteBuffer();
        for (Element child : element.getChildren()) {
            byte[] bytes = ElementKit.matchConverterToBytes(child, device);
            buffer.write(bytes);
        }
        return buffer.toByteArray();
    }
}
