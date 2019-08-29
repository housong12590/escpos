package com.cin.pos.convert;


import com.cin.pos.device.Device;
import com.cin.pos.element.Table;
import com.cin.pos.orderset.OrderSet;
import com.cin.pos.util.ByteBuffer;
import com.cin.pos.util.StringUtils;

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
        buffer.write(orderSet.alignLeft());
        buffer.write(orderSet.cancelEmphasize());
        buffer.write(orderSet.cancelUnderline());
        buffer.write(orderSet.textSizeX1());
        for (Table.TR tr : trs) {
            calculateTdWidth(tr, charLen);
            if (tr.isBold()) {
                buffer.write(orderSet.emphasize());
            } else {
                buffer.write(orderSet.cancelEmphasize());
            }
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
