package com.hstmpl.escpos.element.generator;

import com.hstmpl.escpos.parser.ElementExample;
import com.hstmpl.escpos.parser.Transform;
import com.hstmpl.escpos.util.ConvertUtils;
import com.hstmpl.escpos.element.Text;
import com.hstmpl.escpos.enums.Align;
import com.hstmpl.escpos.enums.Repeat;
import com.hstmpl.escpos.enums.Size;
import com.hstmpl.escpos.parser.Property;

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
