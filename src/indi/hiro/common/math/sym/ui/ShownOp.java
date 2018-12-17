package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class ShownOp {

    @Deprecated
    public static ShownOp catL(ShownOp base, String s) {
        if (base instanceof ShownString) {
            ((ShownString) base).catL(s);
        } else if (base instanceof ShownArray) {
            ShownArray baseS = (ShownArray) base;
            ShownOp baseSS = baseS.getFirst();
            if (baseSS instanceof ShownString) {
                ((ShownString) baseSS).catL(s);
            } else if (baseSS == null) {
                return new ShownString(s);
            } else {
                baseS.addFirst(new ShownString(s));
            }
        } else {
            base = new ShownArray(new ShownString(s), base);
        }
        return base;
    }

    @Deprecated
    public static ShownOp catR(ShownOp base, String s) {
        if (base instanceof ShownString) {
            ((ShownString) base).catR(s);
        } else if (base instanceof ShownArray) {
            ShownArray baseS = (ShownArray) base;
            ShownOp baseSS = baseS.getLast();
            if (baseSS instanceof ShownString) {
                ((ShownString) baseSS).catR(s);
            } else if (baseSS == null) {
                return new ShownString(s);
            } else {
                baseS.addLast(new ShownString(s));
            }
        } else {
            base = new ShownArray(base, new ShownString(s));
        }
        return base;
    }

    @Deprecated
    public static ShownOp addSuperScript(ShownOp base, ShownOp addition) {
        if (base instanceof ShownRearScript) {
            ShownRearScript baseS = (ShownRearScript) base;
            ShownOp baseSS = baseS.getSuperScript();
            if (baseSS == null) {
                baseS.setSuperScript(addition);
            } else if (baseSS instanceof ShownArray) {
                ((ShownArray) baseSS).addLast(addition);
            } else {
                baseS.setSuperScript(new ShownArray(baseSS, addition));
            }
        } else {
            base = new ShownRearScript(base, addition);
        }
        return base;
    }

    protected Rectangle2D.Float measuredRect = new Rectangle2D.Float();

    protected void updateRect(Rectangle2D rect) {
        if (rect instanceof Rectangle2D.Float) {
            measuredRect = (Rectangle2D.Float) rect;
        } else {
            measuredRect.setRect(rect);
        }
    }

    public void simplify(ShownSimplifyConfiguration config) {

    }

    public abstract float textSize();

    public abstract void assignSize(float textSize, TextLevelAcc acc);

    public abstract void assignSize(float textSize);

    public float getWidth() {
        return measuredRect.width;
    }

    public float getHeight() {
        return measuredRect.height;
    }

    public float getUpH() {
        return -measuredRect.y;
    }

    public float getDownH() {
        return measuredRect.y + measuredRect.height;
    }

    public abstract void paint(Graphics2D g, float x, float y);
}
