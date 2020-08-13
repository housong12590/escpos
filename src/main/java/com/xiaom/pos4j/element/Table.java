package com.xiaom.pos4j.element;


import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hous
 */
public class Table extends Element {

    private List<TR> trs;

    public Table() {
        trs = new ArrayList<>();
    }

    public List<TR> getTrs() {
        return trs;
    }

    public void addTr(TR tr) {
        trs.add(tr);
    }

    @Override
    public void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {

    }

    public static class TR {

        private List<TD> tds;
        private boolean bold = false;
        private Size size = Size.normal;

        public TR() {
            tds = new ArrayList<>();
        }

        public List<TD> getTds() {
            return tds;
        }

        public void addTd(TD td) {
            this.tds.add(td);
        }

        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }


    }

    public static class TD {

        private String value = "";
        private int weight = 1;
        private Align align = Align.LEFT;
        private int width;

        public TD() {

        }

        public TD(AttributeSet attr) {
            this.value = attr.getAttributeValue(Attribute.VALUE, this.value);
            this.weight = attr.getIntValue(Attribute.WEIGHT, this.weight);
            this.align = Align.of(attr.getAttributeValue(Attribute.ALIGN), this.align);
        }

        public TD(String value, int weight, Align align, int width) {
            this.value = value;
            this.weight = weight;
            this.align = align;
            this.width = width;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
