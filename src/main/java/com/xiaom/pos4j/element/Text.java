package com.xiaom.pos4j.element;


import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Repeat;
import com.xiaom.pos4j.enums.Size;

/**
 * @author hous
 */
public class Text extends Element {

    private String value = "";
    private Repeat repeat = Repeat.none;
    private Align align = Align.left;
    private Size size = Size.w1h1;
    private boolean bold;
    private boolean underline;

    public Text() {
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public String toString() {
        return "Text{" +
                "value='" + value + '\'' +
                ", repeat=" + repeat +
                ", align=" + align +
                ", size=" + size +
                ", bold=" + bold +
                ", underline=" + underline +
                '}';
    }
}
