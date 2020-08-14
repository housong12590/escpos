package com.xiaom.pos4j.element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hous
 */
public class Group extends Element {

    private List<Element> children;

    public Group() {
        children = new ArrayList<>();
    }

    public void addChildren(Element element) {
        children.add(element);
    }

    public List<Element> getChildren() {
        return children;
    }
}
