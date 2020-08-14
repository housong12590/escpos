package com.xiaom.pos4j.element.generator;

import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Repeat;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.parser.Transform;
import com.xiaom.pos4j.util.ConvertUtils;

public class TextGenerator implements Generator<Text> {

    @Override
    public Text create(ElementExample example, Transform transform, Object env) {
        Text text = new Text();
        parseMargin(text, example);
        for (Property property : example.getProperties()) {
            String name = property.getName();
            String value = property.getValue();
            switch (name) {
                case "size":
                    text.setSize(Size.of(value, Size.w1h1));
                    break;
                case "bold":
                    boolean bold = ConvertUtils.toBool(value, false);
                    text.setBold(bold);
                    break;
                case "align":
                    text.setAlign(Align.of(value, Align.left));
                    break;
                case "value":
                    String apply = property.apply(transform, env);
                    text.setValue(apply);
                    break;
                case "repeat":
                    Repeat repeat = Repeat.of(value, Repeat.none);
                    text.setRepeat(repeat);
                    break;
            }
        }
        return text;
    }
}
