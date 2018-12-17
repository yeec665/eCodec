package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.geom.Line2D;

public class ShownFractionLine extends ShownOp {

    public static final float LINE_HW = 0.5f;
    public static final float LINE_HH = 0.1f;

    protected ShownOp sopUp, sopDown;
    protected float lineHalfHeight = ShownString.DEFAULT_TEXT_SIZE * LINE_HH;

    public ShownFractionLine(ShownOp up, ShownOp down) {
        sopUp = up;
        sopDown = down;
    }

    @Override
    public float textSize() {
        return Math.max(sopUp.textSize(), sopDown.textSize());
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        sopUp.assignSize(textSize, acc);
        sopDown.assignSize(textSize, acc);
    }

    @Override
    public void assignSize(float textSize) {
        sopUp.assignSize(textSize);
        sopDown.assignSize(textSize);
        measuredRect.width = textSize * LINE_HW + Math.max(sopUp.getWidth(), sopDown.getWidth());
        lineHalfHeight = textSize * LINE_HH;
        float upH = lineHalfHeight + sopUp.getHeight();
        float downH = lineHalfHeight + sopDown.getHeight();
        measuredRect.y = -upH;
        measuredRect.height = upH + downH;
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        sopUp.paint(g, x + 0.5f * (measuredRect.width - sopUp.getWidth()),
                y - lineHalfHeight - sopUp.getDownH());
        sopDown.paint(g, x + 0.5f * (measuredRect.width - sopDown.getWidth()),
                y + lineHalfHeight + sopDown.getUpH());
        g.setStroke(StrokePool.getRectStroke(0.5f * lineHalfHeight));
        g.draw(new Line2D.Float(x + lineHalfHeight, y, x + measuredRect.width - lineHalfHeight, y));
    }
}
