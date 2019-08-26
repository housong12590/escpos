package com.cin.pos.element;


import com.cin.pos.common.Dict;
import com.cin.pos.element.exception.TemplateParseException;
import com.cin.pos.parser.attr.AttributeSet;

import java.util.LinkedList;

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
    public void parser(AttributeSet attrs, Dict data) throws TemplateParseException {
        super.parser(attrs, data);
        for (AttributeSet attributeSet : attrs.getAttributeSets()) {
            Text text = new Text();
            text.parser(attributeSet, data);
            addText(text);
        }
    }
}
