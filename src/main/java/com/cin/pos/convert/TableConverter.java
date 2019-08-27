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
            String[][] rows = toRows(tr);
//            List<List<String>> rows = toRows(tr);
            outRows(rows, buffer, device);
        }
        return buffer.toByteArray();
    }

//    private void outRows(List<List<String>> rows, ByteBuffer buffer, Device device) {
//        OrderSet orderSet = device.getOrderSet();
//        for (List<String> row : rows) {
//            for (String s : row) {
//                buffer.write(s.getBytes(device.getCharset()));
//            }
//            buffer.write(orderSet.newLine());
//        }
//    }

    private void outRows(String[][] rows, ByteBuffer buffer, Device device) {
        OrderSet orderSet = device.getOrderSet();
        for (String[] row : rows) {
            for (String s : row) {
                buffer.write(s.getBytes(device.getCharset()));
            }
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

    private String[][] toRows(Table.TR tr) {
        List<Table.TD> tds = tr.getTds();
        int tdCount = tds.size();
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
        String[][] result = new String[maxLine][tdCount];
        for (int i = 0; i < maxLine; i++) {
            String[] row = new String[tdCount];
            for (int j = 0; j < tdCount; j++) {
                Table.TD td = tds.get(j);
                List<String> valueList = temp.get(td);
                String cell;
                if (i < valueList.size()) {
                    cell = valueList.get(i);
                } else {
                    cell = StringUtils.emptyLine(td.getWidth());
                }
                row[j] = cell;
            }
            result[i] = row;
        }
        return result;
    }


//    private List<List<String>> toRows(Table.TR tr) {
//        List<Table.TD> tds = tr.getTds();
//        List<List<String>> cells = new LinkedList<>();
//        List<List<String>> rows = new LinkedList<>();
//        int maxLine = 0;
//        for (Table.TD td : tds) {
//            String value = td.getValue();
//            int width = td.getWidth();
//            List<String> splitValue = StringUtils.splitOfGBKLength(value, width);
//            for (int j = 0; j < splitValue.size(); j++) {
//                String rowStr = splitValue.get(j);
//                if (td.getAlign() == Align.center) {
//                    rowStr = StringUtils.fillBlankBoth2GBKLength(rowStr, width);
//                } else if (td.getAlign() == Align.right) {
//                    rowStr = StringUtils.fillBlankLeft2GBKLength(rowStr, width);
//                } else if (td.getAlign() == Align.left) {
//                    rowStr = StringUtils.fillBlankRight2GBKLength(rowStr, width);
//                }
//                splitValue.set(j, rowStr);
//                if (splitValue.size() > maxLine) {
//                    maxLine = splitValue.size();
//                }
//            }
//            cells.add(splitValue);
//        }
//        for (List<String> cell : cells) {
//            int diffLine = maxLine - cell.size();
//            if (diffLine > 0) {
//                for (int j = 0; j < diffLine; j++) {
//                    StringBuilder sb = new StringBuilder();
//                    String s = cell.get(0);
//                    for (int k = 0; k < s.length(); k++) {
//                        sb.append(" ");
//                    }
//                    cell.add(sb.toString());
//                }
//            }
//        }
//        for (int i = 0; i < maxLine; i++) {
//            List<String> row = new ArrayList<>();
//            for (List<String> cell : cells) {
//                row.add(cell.get(i));
//            }
//            rows.add(row);
//        }
//        return rows;
//    }
}
