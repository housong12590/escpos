package com.xiaom.pos4j.element.generator;

import com.xiaom.pos4j.element.Section;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.ElementKit;
import com.xiaom.pos4j.parser.Transform;

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
