package com.hstmpl.escpos.element;


import java.util.LinkedList;


/**
 * @author hous
 */
public class Section extends Element {

    private LinkedList<Text> texts;

    public Section() {
        texts = new LinkedList<>();
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
    public String toString() {
        return "Section{" +
                "texts=" + texts +
                '}';
    }
}
