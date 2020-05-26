package com.xiaom.pos4j.convert;



import com.xiaom.pos4j.element.Section;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.orderset.OrderSet;

/**
 * @author hous
 */
public class SectionConverter implements Converter<Section> {

    @Override
    public byte[] toBytes(Device device, Section section) {
        OrderSet orderSet = device.getOrderSet();
        ByteBuffer buffer = new ByteBuffer();
        int marginTop = section.getMarginTop();
        if (marginTop > 0) {
            buffer.write(orderSet.paperFeed(marginTop));
        }
        for (Text text : section.getTexts()) {
            byte[] bytes = ConverterKit.matchConverterToBytes(text, device);
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
