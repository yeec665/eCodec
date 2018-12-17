package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class ShownHat extends ShownOp {

    public static final float H_RATE = 0.25f;

    public static final int HAT_STYLE_BAR = 0;
    public static final int HAT_STYLE_DOT = 1;
    public static final int HAT_STYLE_DOUBLE_DOT = 2;
    public static final int HAT_STYLE_ARROW_LR = 3;
    public static final int HAT_STYLE_ARROW_L = 4;
    public static final int HAT_STYLE_ARROW_R = 5;
    public static final int HAT_STYLE_SIMPLE_ARROW_LR = 6;
    public static final int HAT_STYLE_SIMPLE_ARROW_L = 7;
    public static final int HAT_STYLE_SIMPLE_ARROW_R = 8;

    protected ShownOp content;
    private int hatStyle;

    public ShownHat(ShownOp content) {
        this.content = content;
    }

    public ShownHat(ShownOp content, int hatStyle) {
        this.content = content;
        this.hatStyle = hatStyle;
    }

    public void setHatStyle(int hatStyle) {
        this.hatStyle = hatStyle;
    }

    @Override
    public float textSize() {
        return content.textSize();
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        content.assignSize(textSize, acc);
    }

    @Override
    public void assignSize(float textSize) {
        content.assignSize(textSize);
        measuredRect.width = content.getWidth();
        measuredRect.height = (1 + H_RATE) * content.getHeight();
        measuredRect.y = -content.getUpH() - H_RATE * content.getHeight();
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        content.paint(g, x, y);
        float unitH = 0.5f * H_RATE * content.getHeight();
        float midY = y + measuredRect.y + unitH;
        unitH = Math.min(unitH, 0.25f * measuredRect.width);
        g.setStroke(StrokePool.getRectStroke(0.4f * unitH));
        switch (hatStyle) {
            case HAT_STYLE_BAR:
                paintBar(g, x, midY);
                break;
            case HAT_STYLE_DOT:
                paintDot(g, x + 0.5f * measuredRect.width, midY, 0.64f * unitH);
                break;
            case HAT_STYLE_DOUBLE_DOT:

            case HAT_STYLE_ARROW_LR:
                paintBar(g, x + unitH, x + measuredRect.width - unitH, midY);
                paintFullArrowL(g, x, midY, unitH);
                paintFullArrowR(g, x + measuredRect.width, midY, unitH);
                break;
            case HAT_STYLE_ARROW_L:
                paintBar(g, x + unitH, x + measuredRect.width, midY);
                paintFullArrowL(g, x, midY, unitH);
                break;
            case HAT_STYLE_ARROW_R:
                paintBar(g, x, x + measuredRect.width - unitH, midY);
                paintFullArrowR(g, x + measuredRect.width, midY, unitH);
                break;
            case HAT_STYLE_SIMPLE_ARROW_LR:
                paintBar(g, x, midY + 0.2f * unitH);
                paintHalfArrowL(g, x, midY, unitH);
                paintHalfArrowR(g, x + measuredRect.width, midY, unitH);
                break;
            case HAT_STYLE_SIMPLE_ARROW_L:
                paintBar(g, x, midY + 0.2f * unitH);
                paintHalfArrowL(g, x, midY, unitH);
                break;
            case HAT_STYLE_SIMPLE_ARROW_R:
                paintBar(g, x, midY + 0.2f * unitH);
                paintHalfArrowR(g, x + measuredRect.width, midY, unitH);
                break;
        }
    }

    private void paintBar(Graphics2D g, float x, float y) {
        g.draw(new Line2D.Float(x, y, x + measuredRect.width, y));
    }

    private void paintBar(Graphics2D g, float x1, float x2, float y) {
        g.draw(new Line2D.Float(x1, y, x2, y));
    }

    private void paintDot(Graphics2D g, float x, float y, float r) {
        g.fill(new Ellipse2D.Float(x - r, y - r, 2 * r, 2 * r));
    }

    private void paintFullArrowL(Graphics2D g, float x, float y, float unitH) {
        g.fill(new TriangleF(x, y, x + 1.8f * unitH, y - unitH, x + 1.8f * unitH, y + unitH));
    }

    private void paintFullArrowR(Graphics2D g, float x, float y, float unitH) {
        g.fill(new TriangleF(x, y, x - 1.8f * unitH, y - unitH, x - 1.8f * unitH, y + unitH));
    }

    private void paintHalfArrowL(Graphics2D g, float x, float y, float unitH) {
        g.fill(new TriangleF(x, y, x + 1.8f * unitH, y - unitH, x + 1.8f * unitH, y));
    }

    private void paintHalfArrowR(Graphics2D g, float x, float y, float unitH) {
        g.fill(new TriangleF(x, y, x - 1.8f * unitH, y - unitH, x - 1.8f * unitH, y));
    }
}
