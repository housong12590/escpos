package com.hstmpl.escpos.element.generator;

import com.hstmpl.escpos.element.Image;
import com.hstmpl.escpos.enums.Align;
import com.hstmpl.escpos.enums.Type;
import com.hstmpl.escpos.parser.ElementExample;
import com.hstmpl.escpos.parser.Property;
import com.hstmpl.escpos.parser.Transform;
import com.hstmpl.escpos.util.ConvertUtils;

import java.util.List;

public class ImageGenerator implements Generator<Image> {

    @Override
    public Image create(ElementExample example, Transform transform, Object env) {
        Image image = new Image();
        List<Property> properties = example.getProperties();
        for (Property property : properties) {
            String value = property.getValue();
            switch (property.getName()) {
                case "width":
                    Integer width = ConvertUtils.toInt(value, 0);
                    image.setWidth(width);
                    break;
                case "height":
                    Integer height = ConvertUtils.toInt(value, 0);
                    image.setHeight(height);
                    break;
                case "type":
                    Type imageType = Type.of(value, Type.image);
                    image.setType(imageType);
                    break;
                case "value":
                    String apply = property.apply(transform, env);
                    image.setValue(apply);
                    break;
                case "align":
                    Align align = Align.of(value, Align.center);
                    image.setAlign(align);
                    break;
                case "cache":

                    break;
            }
        }
        Type type = image.getType();
        int[] pixels = type.createPixels(image);
        image.setPixels(pixels);
        return image;
    }
}
