package com.cin.pos.element;


import com.cin.pos.convert.ConverterKit;
import com.cin.pos.device.Device;
import com.cin.pos.util.ByteBuffer;

import java.util.LinkedList;
import java.util.List;

/**
 * 此类位要打印的单个单据内容的集合容器，
 */
public class Document {

    private LinkedList<Element> elements = new LinkedList<>();


    public List<Element> getElements() {
        return elements;
    }

    public boolean addElement(Element element) {
        return this.elements.add(element);
    }

    public byte[] toBytes(Device device) {
        ByteBuffer buffer = new ByteBuffer();
        for (Element element : elements) {
            byte[] bytes = ConverterKit.matchConverterToBytes(element, device);
            buffer.write(bytes);
        }
        return buffer.toByteArray();
    }
}
