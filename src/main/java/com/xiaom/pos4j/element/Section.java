package com.xiaom.pos4j.element;


import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;

import java.util.LinkedList;
import java.util.Map;


/**
 * @author hous
 */
public class Section extends Element {

    private LinkedList<Text> texts = new LinkedList<>();

    public Section() {

    }

    public LinkedList<Text> getTexts() {
        for (Text text : texts) {
            text.getRepeat().texts = texts;
        }
        return texts;
    }

    public void addText(Text text) {
        text.setNewLine(false);
        texts.add(text);
    }


    @Override
    public void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {
        for (AttributeSet attributeSet : attrs.getAttributeSets()) {
            Text text = new Text();
            text.parser(attributeSet, data);
            addText(text);
        }
    }
}
