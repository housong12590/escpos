package com.ciin.pos.convert;



import com.ciin.pos.element.Section;
import com.ciin.pos.element.Text;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.device.Device;
import com.ciin.pos.orderset.OrderSet;


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
