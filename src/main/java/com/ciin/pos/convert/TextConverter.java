package com.ciin.pos.convert;


import com.ciin.pos.element.Text;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.device.Device;
import com.ciin.pos.element.Align;
import com.ciin.pos.element.Size;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.StringUtils;

import java.util.List;


public class TextConverter implements Converter<Text> {
    @Override
    public byte[] toBytes(Device device, Text text) {
        OrderSet orderSet = device.getOrderSet();
        if (StringUtils.isEmpty(text.getValue())) {
            return new byte[0];
        }
        ByteBuffer buffer = new ByteBuffer();

        // 距离上面的无素 有多少行的距离
        int marginTop = text.getMarginTop();
        if (marginTop > 0) {
            buffer.write(orderSet.paperFeed(marginTop));
        }

        // 文字加粗
        buffer.write(orderSet.bold(text.isBold()));

        // 文字下划线
        boolean underline = text.isUnderline();
        buffer.write(orderSet.underline(underline));

        // 文字大小
        Size size = text.getSize();
        buffer.write(orderSet.textSize(size));

        // 文字对齐方向
        Align align = text.getAlign();
        buffer.write(orderSet.align(align));

        // 处理文字
        String value = handleString(device, text);

        int width = text.getWidth();
        if (width != Text.WARP_CONTENT) {
            //TODO
        }

        // 写入文字
        buffer.write(value.getBytes(device.getCharset()));

        // 是否需要换行
        boolean newLine = text.isNewLine();
        if (newLine) {
            buffer.write(orderSet.newLine());
        }


        // 距离下面的无素 有多少行的距离
        int marginBottom = text.getMarginBottom();
        if (marginBottom > 0) {
            buffer.write(orderSet.paperFeed(marginBottom));
        }
        return buffer.toByteArray();
    }

    private String handleString(Device device, Text text) {
        String value = text.getValue();
        int charLen = device.getPaper().getCharLen();
        Text.Repeat repeat = text.getRepeat();
        StringBuilder sb = new StringBuilder(text.getValue());
        int marginLeft = text.getMarginLeft();
        int marginRight = text.getMarginRight();
        if (repeat == Text.Repeat.auto) {
            String str = autoRepeatText(device, repeat.texts, text);
            sb.append(str);
        } else if (repeat == Text.Repeat.fill) {
            // 把一行填充满
            Size size = text.getSize();
            int length = StringUtils.lengthOfGBK(value) * size.w;
            int dstLen = (charLen - length - marginLeft - marginRight) / length;
            for (int i = 0; i < dstLen; i++) {
                sb.append(value);
            }
        } else if (repeat == Text.Repeat.count) {
            // 重复固定次数
            for (int i = 0; i < repeat.value; i++) {
                sb.append(value);
            }
        } else if (repeat == Text.Repeat.none) {
            // 不重复什么都不做
        }
        // 处理左右边距
        if (marginLeft == 0 && marginRight == 0) {
            return sb.toString();
        }
        StringBuilder leftBlank = new StringBuilder();
        for (int i = 0; i < marginLeft; i++) {
            leftBlank.append(" ");
        }
        StringBuilder rightBlank = new StringBuilder();
        for (int i = 0; i < marginRight; i++) {
            rightBlank.append(" ");
        }
        int leftBlankLen = leftBlank.length();
        int rightBlankLen = rightBlank.length();
        // 一行实际内容的长度
        int lineMaxLen = charLen - leftBlankLen - rightBlankLen;

        List<String> stringList = StringUtils.splitOfGBKLength(sb.toString(), lineMaxLen);
        sb.delete(0, sb.length());
        for (String str : stringList) {
            sb.append(leftBlank).append(str).append(rightBlank);
        }
        return sb.toString();
    }


    private String autoRepeatText(Device device, List<Text> texts, Text text) {
        // 当前行能容纳的最大字符长度
        int charLen = device.getPaper().getCharLen();
        // 如果在section标签内,兄弟元素所占用的宽度
        int brotherLen = 0;
        int marginLeft = text.getMarginLeft();
        int marginRight = text.getMarginRight();
        // 自己所占用的宽度 字体变大时,所占用的宽度也会改变
        int selfLen = StringUtils.lengthOfGBK(text.getValue()) * text.getSize().w;
        // texts 为空时 ,说明没有在section 标签内, auto当成fill来处理
        int repeatElementNum = 1;
        if (texts != null) {
            for (Text t1 : texts) {
                if (t1 != text) {
                    if (t1.getRepeat() == Text.Repeat.auto) {
                        repeatElementNum++;
                    }
                    String value = t1.getValue();
                    Size size = t1.getSize();
                    brotherLen += StringUtils.lengthOfGBK(value) * size.w;
                    brotherLen += t1.getMarginLeft();
                    brotherLen += t1.getMarginRight();
                }
            }
        }
        // 重复次数
        int repeatCount = (charLen - brotherLen - selfLen - marginLeft - marginRight) / selfLen / repeatElementNum;
        if (repeatElementNum != 1) {
            for (Text t1 : texts) {
                if (t1.getRepeat() == Text.Repeat.auto) {
                    Text.Repeat repeat = Text.Repeat.count;
                    repeat.value = repeatCount;
                    t1.setRepeat(repeat);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeatCount; i++) {
            sb.append(text.getValue());
        }
        return sb.toString();
    }
}
