package com.xiaom.pos4j.parser;


import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.util.ByteBuffer;

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
        StyleSheet sheet = new StyleSheet(device.getOrderSet());
        for (Element element : elements) {
            byte[] bytes = ElementKit.matchConverterToBytes(element, sheet, device);
            buffer.write(bytes);
        }
        return buffer.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Element element : elements) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
