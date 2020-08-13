package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.util.ConvertUtils;
import com.xiaom.pos4j.v3.ElementExample;
import com.xiaom.pos4j.v3.Property;
import com.xiaom.pos4j.v3.Transform;

public class TextGenerator implements Generator<Text> {

    @Override
    public Text create(ElementExample example, Transform transform, Object env) {
        Text text = new Text();
        for (Property property : example.getProperties()) {
            String name = property.getName();
            String value = property.getValue();
            switch (name) {
                case "size":
                    text.setSize(Size.of(value, Size.normal));
                    break;
                case "bold":
                    boolean bold = ConvertUtils.toBool(value, false);
                    text.setBold(bold);
                    break;
                case "align":
                    text.setAlign(Align.of(value, Align.LEFT));
                    break;
                case "value":
                    String apply = property.apply(transform, env);
                    text.setValue(apply);
                    break;
            }
        }
        return text;
    }
}
