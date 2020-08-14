package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Table;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Placeholder;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.util.ConvertUtils;
import com.xiaom.pos4j.v3.*;

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
            }
        }
        Property listProperty = example.getProperty("list");
        boolean isRepeat = listProperty != null;
        for (ElementExample exampleChild : example.getChildren()) {
            Table.TD td = createTableTd(isRepeat, exampleChild, transform, env);
            tr.addTd(td);
        }
        if (!isRepeat) {
            table.addTr(tr);
            return;
        }
        Property itemProperty = example.getProperty("item");
        List<Placeholder> placeholders = listProperty.getPlaceholders();
        if (placeholders.isEmpty()) {
            return;
        }
        Object value = placeholders.get(0).getVariable().execute(transform, env);
        Dict newEnv = Dict.create(env);
        if (!(value instanceof List)) {
            return;
        }
        List<?> list = (List<?>) value;
        MapTransform mapTransform = MapTransform.get();
        for (Object item : list) {
            newEnv.put(itemProperty.getValue(), item);
            Table.TR tr1 = new Table.TR();
            tr1.setSize(tr.getSize());
            tr1.setBold(tr.isBold());

            for (int i = 0; i < tr.getTds().size(); i++) {
                Table.TD td = tr.getTds().get(i);
                Table.TD td1 = new Table.TD();
                td1.setAlign(td.getAlign());
                td1.setWeight(td.getWeight());
                Object o = mapTransform.get(td.getValue(), newEnv);
                td1.setValue(ConvertUtils.toString(o));
                tr1.addTd(td1);
            }
            table.addTr(tr1);
        }
    }

    private Table.TD createTableTd(boolean isRepeat, ElementExample example, Transform transform, Object env) {
        List<Property> properties = example.getProperties();
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
                    if (isRepeat) {
                        td.setValue(property.getPlaceholders().get(0).getVariable().getVariable());
                    } else {
                        String apply = property.apply(transform, env);
                        td.setValue(apply);
                    }
                    break;
            }
        }
        return td;
    }
}
