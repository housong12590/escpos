package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.element.Image;
import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Type;
import com.xiaom.pos4j.util.ConvertUtils;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.v3.Transform;

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
