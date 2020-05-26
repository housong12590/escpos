package com.xiaom.pos4j.element;


import com.xiaom.pos4j.convert.ConverterKit;
import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.device.Device;

import java.util.LinkedList;
import java.util.List;

/**
 * 此类位要打印的单个单据内容的集合容器，
 *
 * @author hous
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