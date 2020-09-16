package com.hstmpl.escpos.element.convert;


import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.parser.StyleSheet;
import com.hstmpl.escpos.util.StringUtils;
import com.hstmpl.escpos.element.Text;
import com.hstmpl.escpos.enums.Repeat;
import com.hstmpl.escpos.enums.Size;
import com.hstmpl.escpos.util.ByteBuffer;

import java.util.List;


/**
 * @author hous
 */
public class TextConverter implements Converter<Text> {
    @Override
    public byte[] toBytes(Device device, StyleSheet styleSheet, Text text) {
        if (StringUtils.isEmpty(text.getValue())) {
            return new byte[0];
        }
        ByteBuffer buffer = new ByteBuffer();

        // 距离上面的无素 有多少行的距离
        styleSheet.paperFeed(buffer, text.getMarginTop());

        // 文字加粗
        styleSheet.bold(buffer, text.isBold());

        // 文字下划线
        styleSheet.underline(buffer, text.isUnderline());

        // 文字大小
        styleSheet.size(buffer, text.getSize());

        // 文字对齐方向
        styleSheet.align(buffer, text.getAlign());

        // 处理文字
        String value = handleString(device, text);

//        int width = text.getWidth();
//        if (width != Text.WARP_CONTENT) {
//            //TODO
//        }

        // 写入文字
        buffer.write(value.getBytes(device.getCharset()));

        // 是否需要换行
        if (text.isNewLine()) styleSheet.newLine(buffer);

        // 距离下面的无素 有多少行的距离
        styleSheet.paperFeed(buffer, text.getMarginBottom());
        return buffer.toByteArray();
    }

    private String handleString(Device device, Text text) {
        String value = text.getValue();
        int charLen = device.getPaper().getCharLen();
        Repeat repeat = text.getRepeat();
        StringBuilder sb = new StringBuilder(text.getValue());
        int marginLeft = text.getMarginLeft();
        int marginRight = text.getMarginRight();
        if (repeat == Repeat.auto) {
            String str = autoRepeatText(device, repeat.texts, text);
            sb.append(str);
        } else if (repeat == Repeat.fill) {
            // 把一行填充满
            Size size = text.getSize();
            int length = StringUtils.lengthOfGBK(value) * size.w;
            int dstLen = (charLen - length - marginLeft - marginRight) / length;
            for (int i = 0; i < dstLen; i++) {
                sb.append(value);
            }
        } else if (repeat == Repeat.count) {
            // 重复固定次数
            for (int i = 0; i < repeat.value; i++) {
                sb.append(value);
            }
        } else if (repeat == Repeat.none) {
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
                    if (t1.getRepeat() == Repeat.auto) {
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
                if (t1.getRepeat() == Repeat.auto) {
                    Repeat repeat = Repeat.count;
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
