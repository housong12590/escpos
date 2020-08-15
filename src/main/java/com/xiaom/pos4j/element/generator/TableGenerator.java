package com.xiaom.pos4j.element.generator;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Table;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Overflow;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.parser.Transform;
import com.xiaom.pos4j.util.ConvertUtils;

import java.util.List;


public class TableGenerator implements Generator<Table> {

    @Override
    public Table create(ElementExample example, Transform transform, Object env) {
        Table table = new Table();
        ElementExample[] children = example.getChildren();
        for (ElementExample trExample : children) {
            parseTableTr(table, trExample, transform, env);
        }
        return table;
    }

    private void parseTableTr(Table table, ElementExample example, Transform transform, Object env) {
        Table.TR tr = new Table.TR();
        List<Property> properties = example.getProperties();
        for (Property property : properties) {
            String value = property.getValue();
            switch (property.getName()) {
                case "bold":
                    Boolean bold = ConvertUtils.toBool(value, false);
                    tr.setBold(bold);
                    break;
                case "size":
                    Size size = Size.of(value, Size.w1h1);
                    tr.setSize(size);
                    break;
                case "overflow":
                    Overflow overflow = Overflow.of(value, Overflow.newline);
                    tr.setOverflow(overflow);
                    break;
            }
        }
        Property listProperty = example.getProperty("list");
        Property itemProperty = example.getProperty("item");
        if (listProperty == null || itemProperty == null) {
            createTableTd(tr, example, transform, env);
            table.addTr(tr);
            return;
        }

        Object value = transform.get(listProperty.getValue(), env);
        if (!(value instanceof List)) {
            return;
        }
        Dict newEnv = Dict.create(env);
        List<?> list = (List<?>) value;
        for (Object item : list) {
            newEnv.put(itemProperty.getValue(), item);
            Table.TR trCopy = new Table.TR();
            trCopy.setOverflow(tr.getOverflow());
            trCopy.setSize(tr.getSize());
            trCopy.setBold(tr.isBold());
            createTableTd(trCopy, example, transform, newEnv);
            table.addTr(trCopy);
        }

    }

    private void createTableTd(Table.TR tr, ElementExample example, Transform transform, Object env) {
        for (ElementExample exampleChild : example.getChildren()) {
            List<Property> properties = exampleChild.getProperties();
            Table.TD td = new Table.TD();
            for (Property property : properties) {
                String value = property.getValue();
                switch (property.getName()) {
                    case "weight":
                        Integer weight = ConvertUtils.toInt(value, 1);
                        td.setWidth(weight);
                        break;
                    case "align":
                        Align align = Align.of(value, Align.left);
                        td.setAlign(align);
                        break;
                    case "value":
                        String apply = property.apply(transform, env);
                        td.setValue(apply);
                        break;
                }
            }
            tr.addTd(td);
        }
    }
}
