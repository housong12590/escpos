package com.xiaom.pos4j.element;

import java.util.ArrayList;
import java.util.List;

public class Block extends Element {

    public List<Element> children;

    public Block() {
        children = new ArrayList<>();
    }

    public List<Element> getChildren() {
        return this.children;
    }

    public void addChildren(Element element) {
        this.children.add(element);
    }

    @Override
    public String toString() {
        return "Block{" +
                "children=" + children +
                '}';
    }
}
