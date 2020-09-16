package com.hstmpl.escpos.element.convert;


import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.element.Image;
import com.hstmpl.escpos.parser.StyleSheet;
import com.hstmpl.escpos.orderset.OrderSet;
import com.hstmpl.escpos.util.ByteBuffer;
import com.hstmpl.escpos.util.ImageCreator;

/**
 * @author hous
 */
public class ImageConverter implements Converter<Image> {

    @Override
    public byte[] toBytes(Device device, StyleSheet styleSheet, Image image) {
        OrderSet orderSet = device.getOrderSet();
        int[] pixels = image.getPixels();
        if (pixels == null || pixels.length == 0) {
            return new byte[0];
        }
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buffer = new ByteBuffer();
        // 距离上面的无素 有多少行的距离
        int marginTop = image.getMarginTop();
        styleSheet.paperFeed(buffer, marginTop);


        styleSheet.align(buffer, image.getAlign());


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
        styleSheet.paperFeed(buffer, marginBottom);
        return buffer.toByteArray();
    }
}
