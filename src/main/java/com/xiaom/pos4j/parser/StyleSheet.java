package com.xiaom.pos4j.parser;

import com.xiaom.pos4j.enums.Align;
import com.xiaom.pos4j.enums.Size;
import com.xiaom.pos4j.orderset.OrderSet;
import com.xiaom.pos4j.util.ByteBuffer;

public class StyleSheet {

    private OrderSet orderSet;
    private Boolean bold;
    private Size size;
    private Align align;
    private Boolean underline;

    public StyleSheet(OrderSet orderSet) {
        this.orderSet = orderSet;
    }

    public void bold(ByteBuffer buffer, boolean bold) {
        if (this.bold != null && this.bold == bold) {
            return;
        }
        buffer.write(orderSet.bold(bold));
        this.bold = bold;
    }

    public void size(ByteBuffer buffer, Size size) {
        if (this.size != null && this.size == size) {
            return;
        }
        buffer.write(orderSet.textSize(size));
        this.size = size;
    }

    public void align(ByteBuffer buffer, Align align) {
        if (this.align != null && this.align == align) {
            return;
        }
        buffer.write(orderSet.align(align));
        this.align = align;
    }

    public void underline(ByteBuffer buffer, boolean underline) {
        if (this.underline != null && this.underline == underline) {
            return;
        }
        buffer.write(orderSet.underline(underline));
        this.underline = underline;
    }

    public void paperFeed(ByteBuffer buffer, int num) {
        if (num <= 0) {
            return;
        }
        buffer.write(orderSet.paperFeed(num));
    }

    public void newLine(ByteBuffer buffer) {
        buffer.write(orderSet.newLine());
    }
}
