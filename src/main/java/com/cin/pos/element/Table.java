package com.cin.pos.element;


import com.cin.pos.Constants;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.LoggerUtil;
import com.cin.pos.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Table extends Element {

    private List<TR> trs = new ArrayList<>();
    private Map<String, Object> data;


    public Table() {

    }


    public List<TR> getTrs() {
        return trs;
    }

    public void setTrs(List<TR> trs) {
        this.trs = trs;
    }


    @Override
    public void parser(AttributeSet attrs, Map<String, Object> data) {
        super.parser(attrs, data);
        this.data = data;
        List<AttributeSet> attributeSets = attrs.getAttributeSets();
        for (AttributeSet trAttrs : attributeSets) {
            TR tr = new TR(trAttrs);
            if (!tr.repeat) {
                this.trs.add(tr);
            }
        }
    }


    public class TR {

        private List<TD> tds = new ArrayList<>();
        private boolean bold = false;
        private boolean repeat;
        private String repeatKey;

        public TR() {

        }

        public TR(AttributeSet attrs) {
            this.bold = attrs.getBooleanValue("bold", false);
            this.repeat = attrs.getBooleanValue("repeat", false);
            this.repeatKey = attrs.getAttributeValue("repeatKey");

            List<AttributeSet> attributeSets = attrs.getAttributeSets();
            for (AttributeSet attributeSet : attributeSets) {
                TD td = new TD(attributeSet);
                tds.add(td);
            }
            if (this.repeat) {
                repeatTr(this);
            }
        }

        private void repeatTr(TR tr) {
            if (data == null) {
                LoggerUtil.error("模版数据为空, 无法进行table repeat操作");
                return;
            }
            Matcher matcher = Constants.REPLACE_PATTERN2.matcher(tr.repeatKey);
            if (matcher.find()) {
                try {
                    String expression = matcher.group(0);
                    String key = matcher.group(1);
                    List<Map> list = (List<Map>) StringUtil.getValue(data, key);
                    if (list == null) {
                        LoggerUtil.error("模版变量 " + expression + " 找不到指定的属性");
                        return;
                    }
                    for (Map item : list) {
                        TR tr1 = new TR();
                        tr.setBold(isBold());
                        for (TD td : tr.getTds()) {
                            TD td1 = new TD(item, td.value);
                            td1.setAlign(td.align);
                            td1.setWeight(td.weight);
                            tr1.addTd(td1);
                            tr1.setBold(bold);
                        }
                        trs.add(tr1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
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
    }

    public class TD {

        private String value = "";
        private int weight = 1;
        private Align align = Align.left;
        private int width;

        public TD(Map item, String value) {
            this.value = StringUtil.subExpression(item, Constants.REPLACE_PATTERN2, value);
        }

        public TD(AttributeSet attrs) {
            this.value = attrs.getAttributeValue("value", this.value);
            this.weight = attrs.getIntValue("weight", this.weight);
            this.align = Align.parserAlign(attrs.getAttributeValue("align"), this.align);
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
