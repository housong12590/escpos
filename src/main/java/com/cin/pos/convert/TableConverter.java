package com.cin.pos.convert;


import com.cin.pos.device.Device;
import com.cin.pos.element.Align;
import com.cin.pos.element.Table;
import com.cin.pos.util.ByteBuffer;
import com.cin.pos.util.StringUtil;
import com.cin.pos.orderset.OrderSet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            List<List<String>> rows = toRows(tr);
            outRows(rows, buffer, device);
        }
        return buffer.toByteArray();
    }

    private void outRows(List<List<String>> rows, ByteBuffer buffer, Device device) {
        OrderSet orderSet = device.getOrderSet();
        for (List<String> row : rows) {
            for (String s : row) {
                try {
                    buffer.write(s.getBytes(device.getCharset()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            buffer.write(orderSet.newLine());
        }
    }


    private void calculateTdWidth(Table.TR tr, int charLen) {
        float allWeight = 0;
        for (Table.TD td : tr.getTds()) {
            allWeight += td.getWeight();
        }
//        float each = charLen / allWeight;
//        List<Table.TD> tds = tr.getTds();
//        int sumWidth = 0;
//        for (Table.TD td : tds) {
//            int width = (int) (td.getWeight() * each);
//            td.setWidth(width);
//            sumWidth += width;
//        }
//        out:
//        while (true) {
//            for (Table.TD td : tds) {
//                if (sumWidth < charLen) {
//                    int width = td.getWidth();
//                    td.setWidth(width + 1);
//                    sumWidth++;
//                } else {
//                    break out;
//                }
//            }
//        }

        float remainder = charLen % allWeight;
        float each = charLen / (allWeight + remainder);
        int wholeLength = 0;
        for (int i = 0; i < tr.getTds().size(); i++) {
            Table.TD td = tr.getTds().get(i);
            float width = (td.getWeight() + remainder / tr.getTds().size()) * each;
            td.setWidth((int) width);
            wholeLength += width;
        }
        if (wholeLength > charLen) {
            throw new RuntimeException("The whole length of the headers is longer than the width of this getPrinter device!");
        }
    }

    private List<List<String>> toRows(Table.TR tr) {
        List<Table.TD> tds = tr.getTds();
        List<List<String>> cells = new LinkedList<>();
        List<List<String>> rows = new LinkedList<>();
        int maxLine = 0;
        for (Table.TD td : tds) {
            String value = td.getValue();
            int width = td.getWidth();
            List<String> splitValue = StringUtil.splitStringLenOfGBK(value, width);
            for (int j = 0; j < splitValue.size(); j++) {
                String rowStr = splitValue.get(j);
                if (td.getAlign() == Align.center) {
                    rowStr = StringUtil.fillBlankBoth2GBKLength(rowStr, width);
                } else if (td.getAlign() == Align.right) {
                    rowStr = StringUtil.fillBlankLeft2GBKLength(rowStr, width);
                } else if (td.getAlign() == Align.left) {
                    rowStr = StringUtil.fillBlankRight2GBKLength(rowStr, width);
                }
                splitValue.set(j, rowStr);
                if (splitValue.size() > maxLine) {
                    maxLine = splitValue.size();
                }
            }
            cells.add(splitValue);
        }
        for (List<String> cell : cells) {
            int diffLine = maxLine - cell.size();
            if (diffLine > 0) {
                for (int j = 0; j < diffLine; j++) {
                    StringBuilder sb = new StringBuilder();
                    String s = cell.get(0);
                    for (int k = 0; k < s.length(); k++) {
                        sb.append(" ");
                    }
                    cell.add(sb.toString());
                }
            }
        }
        for (int i = 0; i < maxLine; i++) {
            List<String> row = new ArrayList<>();
            for (List<String> cell : cells) {
                row.add(cell.get(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
