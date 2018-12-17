package indi.hiro.common.math.sym.ui;

import java.awt.*;

public class ShownRearScript extends ShownOp {

    public static final float RATE = 0.65f;

    public static final float I_RATE = 1 / RATE;

    protected ShownOp sopBase, sopSup, sopSub;

    public ShownRearScript(ShownOp base, ShownOp exponent) {
        sopBase = base;
        sopSup = exponent;
    }

    public ShownRearScript(ShownOp base, ShownOp sup, ShownOp sub) {
        sopBase = base;
        sopSup = sup;
        sopSub = sub;
    }

    public void setSuperScript(ShownOp sup) {
        sopSup = sup;
    }

    public void setSubScript(ShownOp sub) {
        sopSub = sub;
    }

    public ShownOp getSuperScript() {
        return sopSup;
    }

    public ShownOp getSubScript() {
        return sopSub;
    }

    @Override
    public float textSize() {
        float ts1 = sopBase.textSize(), ts2;
        if (sopSup != null && (ts2 = I_RATE * sopSup.textSize()) > ts1) {
            ts1 = ts2;
        }
        if (sopSub != null && (ts2 = I_RATE * sopSub.textSize()) > ts1) {
            ts1 = ts2;
        }
        return ts1;
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        sopBase.assignSize(textSize, acc);
        if (sopSup != null) {
            sopSup.assignSize(RATE * textSize, acc);
        }
        if (sopSub != null) {
            sopSub.assignSize(RATE * textSize, acc);
        }
    }

    @Override
    public void assignSize(float textSize) {
        sopBase.assignSize(textSize);
        float maxW = 0, upH, downH;
        if (sopSup != null) {
            sopSup.assignSize(RATE * textSize);
            maxW = Math.max(maxW, sopSup.getWidth());
            upH = sopBase.getUpH() + sopSup.getUpH();
        } else {
            upH = sopBase.getUpH();
        }
        if (sopSub != null) {
            sopSub.assignSize(RATE * textSize);
            maxW = Math.max(maxW, sopSub.getWidth());
            downH = sopBase.getDownH() + sopSub.getDownH();
        } else {
            downH = sopBase.getDownH();
        }
        measuredRect.width = sopBase.getWidth() + maxW;
        measuredRect.y = -upH;
        measuredRect.height = upH + downH;
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        sopBase.paint(g, x, y);
        if (sopSup != null) {
            sopSup.paint(g, x + sopBase.getWidth(), y - sopBase.getUpH());
        }
        if (sopSub != null) {
            sopSub.paint(g, x + sopBase.getWidth(), y + sopBase.getDownH());
        }
    }
}
