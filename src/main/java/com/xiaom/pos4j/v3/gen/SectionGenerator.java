package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.element.Section;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.v3.Transform;

public class SectionGenerator implements Generator<Section> {

    @Override
    public Section create(ElementExample example, Transform transform, Object env) {
        Section section = new Section();
        for (ElementExample el : example.getChildren()) {
            TextGenerator g = new TextGenerator();
            Text text = g.create(el, transform, env);
            section.addText(text);
        }
        return section;
    }
}
