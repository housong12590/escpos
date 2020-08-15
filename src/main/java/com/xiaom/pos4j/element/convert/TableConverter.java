package com.xiaom.pos4j.element.convert;


import com.xiaom.pos4j.device.Device;
import com.xiaom.pos4j.element.Table;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.orderset.OrderSet;
import com.xiaom.pos4j.parser.StyleSheet;
import com.xiaom.pos4j.util.ByteBuffer;
import com.xiaom.pos4j.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hous
 */
public class TableConverter implements Converter<Table> {
    @Override
    public byte[] toBytes(Device device, StyleSheet styleSheet, Table table) {
        ByteBuffer buffer = new ByteBuffer();
        int charLen = device.getPaper().getCharLen();
        List<Table.TR> trs = table.getTrs();

        styleSheet.align(buffer, Align.left);
        styleSheet.underline(buffer, false);

        for (Table.TR tr : trs) {
            calculateTdWidth(tr, charLen);

            styleSheet.bold(buffer, tr.isBold());
            styleSheet.size(buffer, tr.getSize());

            String[] rows = toRows(tr, tr.getSize());
            outRows(rows, buffer, device);
        }
        return buffer.toByteArray();
    }

    private void outRows(String[] rows, ByteBuffer buffer, Device device) {
        OrderSet orderSet = device.getOrderSet();
        for (String row : rows) {
            buffer.write(row.getBytes(device.getCharset()));
            buffer.write(orderSet.paperFeed(1));
        }
    }


    private void calculateTdWidth(Table.TR tr, int charLen) {
        int weightSum = 0;
        for (Table.TD td : tr.getTds()) {
            weightSum += td.getWeight();
        }
        int remainder = charLen % weightSum;
        int each = charLen / weightSum;
        int tdCount = tr.getTds().size();
        int y = remainder / tdCount;
        int x = remainder % tdCount;
        for (Table.TD td : tr.getTds()) {
            int width = each * td.getWeight() + y;
            if (x != 0) {
                width++;
                x--;
            }
            td.setWidth(width);
        }
    }

    private String[] toRows(Table.TR tr, Size size) {
        List<Table.TD> tds = tr.getTds();
        Map<Table.TD, List<String>> temp = new HashMap<>();
        int maxLine = 0;
        for (Table.TD td : tds) {
            String value = td.getValue();
            int width = td.getWidth();
            List<String> valueList = StringUtils.splitOfGBKLength(value, width, td.getAlign(), size);
            temp.put(td, valueList);
            if (valueList.size() > maxLine) {
                maxLine = valueList.size();
            }
        }
        String[] result = new String[maxLine];
        for (int i = 0; i < maxLine; i++) {
            StringBuilder row = new StringBuilder();
            for (Table.TD td : tds) {
                List<String> valueList = temp.get(td);
                if (i < valueList.size()) {
                    row.append(valueList.get(i));
                } else {
                    row.append(StringUtils.emptyLine(td.getWidth()));
                }
            }
            result[i] = row.toString();
        }
        return result;
    }
}
