package com.hstmpl.escpos.element.generator;

import com.hstmpl.escpos.element.Section;
import com.hstmpl.escpos.element.Text;
import com.hstmpl.escpos.parser.ElementExample;
import com.hstmpl.escpos.parser.ElementKit;
import com.hstmpl.escpos.parser.Transform;

public class SectionGenerator implements Generator<Section> {

    @Override
    public Section create(ElementExample example, Transform transform, Object env) {
        Section section = new Section();
        for (ElementExample el : example.getChildren()) {
            Text text = ElementKit.getElement(el, transform, env);
            section.addText(text);
        }
        return section;
    }
}
