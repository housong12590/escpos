package com.cin.pos.convert;


import com.cin.pos.device.Device;
import com.cin.pos.element.Align;
import com.cin.pos.element.Text;
import com.cin.pos.util.ByteBuffer;
import com.cin.pos.util.StringUtil;
import com.cin.pos.orderset.OrderSet;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class TextConverter implements Converter<Text> {
    @Override
    public byte[] toBytes(Device device, Text text) {
        OrderSet orderSet = device.getOrderSet();
        if (StringUtil.isEmpty(text.getValue())) {
            return new byte[0];
        }
        ByteBuffer buffer = new ByteBuffer();

        // 距离上面的无素 有多少行的距离
        int marginTop = text.getMarginTop();
        if (marginTop > 0) {
            buffer.write(orderSet.paperFeed(marginTop));
        }

        // 文字加粗
        boolean bold = text.isBold();
        if (bold) {
            buffer.write(orderSet.emphasize());
        } else {
            buffer.write(orderSet.cancelEmphasize());
        }

        // 文字下划线
        boolean underline = text.isUnderline();
        if (underline) {
            buffer.write(orderSet.underline());
        } else {
            buffer.write(orderSet.cancelUnderline());
        }

        // 文字大小
        Text.Size size = text.getSize();
        if (size == Text.Size.normal) {
            buffer.write(orderSet.textSizeX1());
        } else if (size == Text.Size.big) {
            buffer.write(orderSet.textSizeX2());
        } else if (size == Text.Size.oversized) {
            buffer.write(orderSet.textSizeX3());
        } else {
            buffer.write(orderSet.testSize(size.w, size.h));
        }

        // 文字对齐方向
        Align align = text.getAlign();
        if (align == Align.left) {
            buffer.write(orderSet.alignLeft());
        } else if (align == Align.center) {
            buffer.write(orderSet.alignCenter());
        } else if (align == Align.right) {
            buffer.write(orderSet.alignRight());
        }

        // 处理文字
        String value = handleString(device, text);

        int width = text.getWidth();
        if (width != Text.WARP_CONTENT) {
            //TODO
        }

        // 写入文字
        try {
            buffer.write(value.getBytes(device.getCharset()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
            Text.Size size = text.getSize();
            int length = StringUtil.lengthOfGBK(value) * size.w;
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

        List<String> stringList = StringUtil.splitStringLenOfGBK(sb.toString(), lineMaxLen);
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
        int selfLen = StringUtil.lengthOfGBK(text.getValue()) * text.getSize().w;
        // texts 为空时 ,说明没有在section 标签内, auto当成fill来处理
        int repeatElementNum = 1;
        if (texts != null) {
            for (Text t1 : texts) {
                if (t1 != text) {
                    if (t1.getRepeat() == Text.Repeat.auto) {
                        repeatElementNum++;
                    }
                    String value = t1.getValue();
                    Text.Size size = t1.getSize();
                    brotherLen += StringUtil.lengthOfGBK(value) * size.w;
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
