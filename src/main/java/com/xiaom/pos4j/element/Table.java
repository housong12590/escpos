package com.xiaom.pos4j.element;


import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.LogUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hous
 */
public class Table extends Element {

    private List<TR> trs = new ArrayList<>();
    private Map<?, ?> data;


    public Table() {

    }

    public List<TR> getTrs() {
        return trs;
    }

    public void setTrs(List<TR> trs) {
        this.trs = trs;
    }


    @Override
    public void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {
        this.data = data;
        List<AttributeSet> attributeSets = attrs.getAttributeSets();
        for (AttributeSet trAttrs : attributeSets) {
            TR tr = new TR();
            tr.parser(trAttrs);
        }
    }


    public class TR {

        private List<TD> tds = new ArrayList<>();
        private boolean bold = false;
        private boolean repeat;
        private String repeatKey;
        private Size size = Size.normal;

        public TR() {

        }

        public boolean isRepeat() {
            return repeat;
        }

        public void setRepeat(boolean repeat) {
            this.repeat = repeat;
        }

        public List<TD> getTds() {
            return tds;
        }

        public void addTd(TD td) {
            this.tds.add(td);
        }

        public void setTds(List<TD> tds) {
            this.tds = tds;
        }

        public String getRepeatKey() {
            return repeatKey;
        }

        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        public void setRepeatKey(String repeatKey) {
            this.repeatKey = repeatKey;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        public void parser(AttributeSet attrs) throws TemplateParseException {
            this.bold = attrs.getBooleanValue(Attribute.BOLD, false);
            this.repeat = attrs.getBooleanValue(Attribute.REPEAT, false);
            this.repeatKey = attrs.getAttributeValue(Attribute.REPEAT_KEY);
            this.size = Size.of(attrs.getAttributeValue(Attribute.SIZE), this.size);
            for (AttributeSet attributeSet : attrs.getAttributeSets()) {
                TD td = new TD(attributeSet);
                tds.add(td);
            }
            if (!this.repeat) {
                trs.add(this);
            } else {
                repeatTr(this);
            }
        }

        private void repeatTr(TR tr) throws TemplateParseException {
            if (data == null) {
                LogUtils.error("模版数据为空, 无法进行table repeat操作");
                return;
            }
            String expression = ExpressionUtils.getExpression(Constants.PARSE_PATTERN, tr.repeatKey);
            if (StringUtils.isEmpty(expression)) {
                throw new TemplateParseException("无效的表达式" + tr.repeatKey);
            }
            Object expressionValue = ExpressionUtils.getValue(data, expression);
            if (expressionValue == null) {
                throw new TemplateParseException(tr.repeatKey + "表达式值为空值");
            }
            if (!(expressionValue instanceof Iterable)) {
                throw new TemplateParseException(tr.repeatKey + "的值不是一个可迭代对象,无法进行遍历");
            }

            try {
                for (Object value : ((Iterable<?>) expressionValue)) {
                    Dict item = Dict.create(value);
                    TR tr1 = new TR();
                    for (TD td : tr.getTds()) {
                        String text = ExpressionUtils.replacePlaceholder(Constants.PARSE_PATTERN, td.getValue(), item);
                        TD td1 = new TD(text, td.weight, td.align, td.width);
                        tr1.addTd(td1);
                        tr1.setBold(bold);
                        tr1.setSize(size);
                    }
                    trs.add(tr1);
                }
            } catch (Exception e) {
                throw new TemplateParseException(tr.repeatKey + " 数据格式不正确", e);
            }
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
