package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.geom.Line2D;

public class ShownBracket extends ShownOp {

    public static final float H_RATE = 0.09f;
    public static final float W_RATE = 0.4f;

    public static final int BRACKET_STYLE_ROUND = 0;
    public static final int BRACKET_STYLE_ROUND_THICK = 1;
    public static final int BRACKET_STYLE_RECT = 2;
    public static final int BRACKET_STYLE_RECT_UP = 3;
    public static final int BRACKET_STYLE_RECT_DOWN = 4;
    public static final int BRACKET_STYLE_VET_LINE = 5;
    public static final int BRACKET_STYLE_DOUBLE_VET_LINE = 6;
    public static final int BRACKET_STYLE_ROUND_RECT = 7;
    public static final int BRACKET_STYLE_RECT_ROUND = 8;
    public static final int BRACKET_STYLE_CURLY = 9;

    protected ShownOp content;
    private float textSize = ShownString.DEFAULT_TEXT_SIZE;
    private int bracketStyle;

    public ShownBracket(ShownOp content) {
        this.content = content;
    }

    public ShownBracket(ShownOp content, int bracketStyle) {
        this.content = content;
        this.bracketStyle = bracketStyle;
    }

    public void setBracketStyle(int bracketStyle) {
        this.bracketStyle = bracketStyle;
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
        this.textSize = textSize;
        content.assignSize(textSize);
        measuredRect.width = content.getWidth() + 2 * W_RATE * textSize;
        measuredRect.height = (1 + 2 * H_RATE) * content.getHeight();
        measuredRect.y = -content.getUpH() - H_RATE * content.getHeight();
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        content.paint(g, x + W_RATE * textSize, y);
        g.setStroke(StrokePool.getRectStroke(0.05f * textSize));
        paintBrackets(g, x + 0.05f * textSize, x + 0.35f * textSize,
                x + measuredRect.width - 0.35f * textSize, x + measuredRect.width - 0.05f * textSize,
                y - getUpH() + 0.05f * textSize, y + getDownH() - 0.05f * textSize);
    }

    private void paintBrackets(Graphics2D g, float x1, float x2, float x3, float x4, float y1, float y2) {
        switch (bracketStyle) {
            case BRACKET_STYLE_ROUND:
                paintRoundBracket(g, x1, x2, y1, y2);
                paintRoundBracket(g, x4, x3, y1, y2);
                break;
            case BRACKET_STYLE_ROUND_THICK:

                break;
            case BRACKET_STYLE_RECT:
                g.draw((new PathBuilderF()).moveTo(x2, y1).lineTo(x1, y1).lineTo(x1, y2).lineTo(x2, y2).finish());
                g.draw((new PathBuilderF()).moveTo(x3, y1).lineTo(x4, y1).lineTo(x4, y2).lineTo(x3, y2).finish());
                break;
            case BRACKET_STYLE_RECT_UP:
                g.draw((new PathBuilderF()).moveTo(x2, y1).lineTo(x1, y1).lineTo(x1, y2).finish());
                g.draw((new PathBuilderF()).moveTo(x3, y1).lineTo(x4, y1).lineTo(x4, y2).finish());
                break;
            case BRACKET_STYLE_RECT_DOWN:
                g.draw((new PathBuilderF()).moveTo(x1, y1).lineTo(x1, y2).lineTo(x2, y2).finish());
                g.draw((new PathBuilderF()).moveTo(x4, y1).lineTo(x4, y2).lineTo(x3, y2).finish());
                break;
            case BRACKET_STYLE_VET_LINE:
                paintVetLine(g, 0.5f * (x1 + x2), y1, y2);
                paintVetLine(g, 0.5f * (x3 + x4), y1, y2);
                break;
            case BRACKET_STYLE_DOUBLE_VET_LINE:
                paintVetLine(g, 0.8f * x1 + 0.2f * x2, y1, y2);
                paintVetLine(g, 0.2f * x1 + 0.8f * x2, y1, y2);
                paintVetLine(g, 0.8f * x3 + 0.2f * x4, y1, y2);
                paintVetLine(g, 0.2f * x3 + 0.8f * x4, y1, y2);
                break;
            case BRACKET_STYLE_ROUND_RECT:
                paintRoundBracket(g, x1, x2, y1, y2);
                g.draw((new PathBuilderF()).moveTo(x3, y1).lineTo(x4, y1).lineTo(x4, y2).lineTo(x3, y2).finish());
                break;
            case BRACKET_STYLE_RECT_ROUND:
                g.draw((new PathBuilderF()).moveTo(x2, y1).lineTo(x1, y1).lineTo(x1, y2).lineTo(x2, y2).finish());
                paintRoundBracket(g, x4, x3, y1, y2);
                break;
            case BRACKET_STYLE_CURLY:
                paintCurlyBracket(g, x1, x2, y1, y2);
                paintCurlyBracket(g, x4, x3, y1, y2);
                break;
        }
    }

    private void paintRoundBracket(Graphics2D g, float x1, float x2, float y1, float y3) {
        //g.draw(new QuadCurve2D.Float(x2, y1, 2 * x1 - x2, 0.5f * (y1 + y3), x2, y3));
        float y2 = 0.5f * (y1 + y3);
        g.draw((new PathBuilderF()).moveTo(x2, y1).quadTo(x1, 0.8f * y1 + 0.2f * y2, x1, y2)
                .quadTo(x1, 0.8f * y3 + 0.2f * y2, x2, y3).finish());
    }

    private void paintCurlyBracket(Graphics2D g, float x1, float x2, float y1, float y3) {
        float y2 = 0.5f * (y1 + y3);
        g.draw((new PathBuilderF()).moveTo(x2, y1).cubicTo(x1, y1, x2, y2, x1, y2).cubicTo(x2, y2, x1, y3, x2, y3).finish());
    }

    private void paintVetLine(Graphics2D g, float x, float y1, float y2) {
        g.draw(new Line2D.Float(x, y1, x, y2));
    }
}
