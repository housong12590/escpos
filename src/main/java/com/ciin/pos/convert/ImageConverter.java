package com.ciin.pos.convert;


import com.ciin.pos.element.Align;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.ImageCreator;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.device.Device;
import com.ciin.pos.element.Image;
import com.ciin.pos.orderset.OrderSet;

public class ImageConverter implements Converter<Image> {

    @Override
    public byte[] toBytes(Device device, Image image) {
        OrderSet orderSet = device.getOrderSet();
        int[] pixels = image.getPixels();
        if (pixels == null || pixels.length == 0) {
            LogUtils.error("图片数据为空!");
            return new byte[0];
        }
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buffer = new ByteBuffer();
        // 距离上面的无素 有多少行的距离
        int marginTop = image.getMarginTop();
        if (marginTop > 0) {
            buffer.write(orderSet.paperFeed(marginTop));
        }

        Align align = image.getAlign();
        buffer.write(orderSet.align(align));

        int bytesNumberOfWidth = width % 8 == 0 ? width / 8 : width / 8 + 1;
        byte xL = (byte) (bytesNumberOfWidth % 256);
        byte xH = (byte) (bytesNumberOfWidth / 256);
        byte yL = (byte) (height % 256);
        byte yH = (byte) (height / 256);

        byte[] dstBitData = new byte[bytesNumberOfWidth * height];
        ImageCreator.convertMultiBytesToSingleBit(width, height, pixels, dstBitData);
        buffer.write(orderSet.printImage((byte) 0x00, xL, xH, yL, yH, dstBitData));
        // 距离下面的无素 有多少行的距离
        int marginBottom = image.getMarginBottom();
        if (marginBottom > 0) {
            buffer.write(orderSet.paperFeed(marginBottom));
        }
        return buffer.toByteArray();
    }
}
