package com.xiaom.pos4j.convert;

import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.Group;
import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.device.Device;

/**
 * @author hous
 */
public class GroupConverter implements Converter<Group> {
    @Override
    public byte[] toBytes(Device device, Group element) {
        ByteBuffer buffer = new ByteBuffer();
        for (Element child : element.getChildren()) {
            byte[] bytes = ConverterKit.matchConverterToBytes(child, device);
            buffer.write(bytes);
        }
        return buffer.toByteArray();
    }
}
