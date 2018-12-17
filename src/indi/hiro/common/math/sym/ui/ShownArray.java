package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.util.LinkedList;

public class ShownArray extends ShownOp {

    protected final LinkedList<ShownOp> contents = new LinkedList<>();

    public ShownArray(ShownOp sop) {
        contents.add(sop);
    }

    public ShownArray(ShownOp a, ShownOp b) {
        contents.add(a);
        contents.add(b);
    }

    public void addFirst(ShownOp sop) {
        contents.addFirst(sop);
    }

    public void addLast(ShownOp sop) {
        contents.addLast(sop);
    }

    public ShownOp get(int index) {
        return contents.get(index);
    }

    public ShownOp getFirst() {
        return contents.isEmpty() ? null : contents.getFirst();
    }

    public ShownOp getLast() {
        return contents.isEmpty() ? null : contents.getLast();
    }

    @Override
    public float textSize() {
        float ts = 0;
        for (ShownOp sop : contents) {
            ts = Math.max(sop.textSize(), ts);
        }
        return ts;
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        for (ShownOp sop : contents) {
            sop.assignSize(textSize, acc);
        }
    }

    @Override
    public void assignSize(float textSize) {
        measuredRect.width = 0;
        float upH = 0, downH = 0;
        for (ShownOp sop : contents) {
            sop.assignSize(textSize);
            measuredRect.width += sop.getWidth();
            upH = Math.max(sop.getUpH(), upH);
            downH = Math.max(sop.getDownH(), downH);
        }
        measuredRect.y = -upH;
        measuredRect.height = upH + downH;
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        for (ShownOp sop : contents) {
            sop.paint(g, x, y);
            x += sop.getWidth();
        }
    }
}
