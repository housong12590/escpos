package com.ciin.pos.convert;


import com.ciin.pos.element.Table;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.device.Device;
import com.ciin.pos.element.Align;
import com.ciin.pos.element.Size;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableConverter implements Converter<Table> {
    @Override
    public byte[] toBytes(Device device, Table table) {
        OrderSet orderSet = device.getOrderSet();
        ByteBuffer buffer = new ByteBuffer();
        int charLen = device.getPaper().getCharLen();
        List<Table.TR> trs = table.getTrs();
        buffer.write(orderSet.align(Align.left));
        buffer.write(orderSet.bold(false));
        buffer.write(orderSet.underline(false));
        buffer.write(orderSet.textSize(Size.normal));
        for (Table.TR tr : trs) {
            calculateTdWidth(tr, charLen);
            buffer.write(orderSet.bold(tr.isBold()));
            String[] rows = toRows(tr);
            outRows(rows, buffer, device);
        }
        return buffer.toByteArray();
    }

    private void outRows(String[] rows, ByteBuffer buffer, Device device) {
        OrderSet orderSet = device.getOrderSet();
        for (String row : rows) {
            buffer.write(row.getBytes(device.getCharset()));
            buffer.write(orderSet.newLine());
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

    private String[] toRows(Table.TR tr) {
        List<Table.TD> tds = tr.getTds();
        Map<Table.TD, List<String>> temp = new HashMap<>();
        int maxLine = 0;
        for (Table.TD td : tds) {
            String value = td.getValue();
            int width = td.getWidth();
            List<String> valueList = StringUtils.splitOfGBKLength(value, width, td.getAlign());
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
