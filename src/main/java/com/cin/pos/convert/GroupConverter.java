package com.cin.pos.convert;

import com.cin.pos.device.Device;
import com.cin.pos.element.Element;
import com.cin.pos.element.Group;
import com.cin.pos.util.ByteBuffer;

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
