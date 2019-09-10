package com.ciin.pos.convert;

import com.ciin.pos.element.Element;
import com.ciin.pos.element.Group;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.device.Device;

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
