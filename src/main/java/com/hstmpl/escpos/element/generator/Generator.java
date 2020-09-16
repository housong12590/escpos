package com.hstmpl.escpos.element.generator;

import com.hstmpl.escpos.element.Element;
import com.hstmpl.escpos.parser.ElementExample;
import com.hstmpl.escpos.parser.Transform;
import com.hstmpl.escpos.util.ConvertUtils;
import com.hstmpl.escpos.parser.Property;

public interface Generator<T extends Element> {

    T create(ElementExample example, Transform transform, Object env);

    default void parseMargin(Element element, ElementExample example) {
        int[] margin = element.getMargin();
        Property marginLeft = example.getProperty("marginLeft");
        if (marginLeft != null) {
            margin[0] = ConvertUtils.toInt(marginLeft.getValue(), 0);
        }
        Property marginTop = example.getProperty("marginTop");
        if (marginTop != null) {
            margin[1] = ConvertUtils.toInt(marginTop.getValue(), 0);
        }
        Property marginRight = example.getProperty("marginRight");
        if (marginRight != null) {
            margin[2] = ConvertUtils.toInt(marginRight.getValue(), 0);
        }
        Property marginBottom = example.getProperty("marginBottom");
        if (marginBottom != null) {
            margin[3] = ConvertUtils.toInt(marginBottom.getValue(), 0);
        }
        element.setMargin(margin);
    }
}
