package com.cin.pos.convert;



import com.cin.pos.device.Device;
import com.cin.pos.element.Section;
import com.cin.pos.element.Text;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.util.ByteBuffer;


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
