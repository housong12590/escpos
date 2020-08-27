package com.xiaom.pos4j.enums;

import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.util.ConvertUtils;

import java.util.List;

/**
 * @author hous
 */
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

    private static final Repeat[] ENUMS = Repeat.values();

    @Override
    public String toString() {
        return this.name();
    }

    public static Repeat of(String value, Repeat repeat) {
        for (Repeat r : ENUMS) {
            if (r.name().equals(value)) {
                return r;
            }
        }
        try {
            Repeat.count.value = ConvertUtils.toInt(value, 0);
            return Repeat.none;
        } catch (Exception e) {
            return repeat;
        }
    }
}
