package com.ciin.pos.element;


import com.ciin.pos.convert.ConverterKit;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.device.Device;

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

    public void addElement(Element element) {
        this.elements.add(element);
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
