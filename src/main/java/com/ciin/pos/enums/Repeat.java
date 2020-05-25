package com.ciin.pos.enums;

import com.ciin.pos.element.Text;
import com.ciin.pos.util.ConvertUtils;

import java.util.List;

public enum Repeat {

    /**
     * 不重复
     */
    none,
    /**
     * 自动
     */
    auto,
    /**
     * 填满
     */
    fill,
    /**
     * 重复固定次数
     */
    count;

    public int value;

    public List<Text> texts;

    @Override
    public String toString() {
        return this.name();
    }

    public static Repeat of(String value, Repeat repeat) {
        for (Repeat r : Repeat.values()) {
            if (r.name().equalsIgnoreCase(value)) {
                return r;
            }
        }
        try {
            Repeat.count.value = ConvertUtils.toInt(value, 0);
            return Repeat.count;
        } catch (Exception e) {
            return repeat;
        }
    }
}
