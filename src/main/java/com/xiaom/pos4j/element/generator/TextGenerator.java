package com.xiaom.pos4j.element.generator;

import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Repeat;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.parser.Transform;
import com.xiaom.pos4j.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

public class TextGenerator implements Generator<Text> {


    @Override
    public void apply(ElementExample example, Transform transform, Object env) {
        Text text = (Text) example.getElement();
        Text newText = text.clone();
        for (CCCCC<Element> mapping : example.getMappings()) {
            mapping.apply(newText, null, transform, env);
        }
    }

    @Override
    public Text create(ElementExample example, Transform transform, Object env) {
        Text text = new Text();
        List<CCCCC<Element>> mappings = new ArrayList<>();
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
                    mappings.add((element, p, transform1, env1) -> element.setValue(p.apply(transform1, env1)));
                    break;
                case "repeat":
                    Repeat repeat = Repeat.of(value, Repeat.none);
                    text.setRepeat(repeat);
                    break;
            }
        }
        example.addMapping(mappings);
        example.setElement(text);
        return text;
    }
}
