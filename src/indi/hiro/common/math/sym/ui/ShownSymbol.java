package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class ShownSymbol extends ShownOp {

    public static final int SYMBOL_PLUS = 0;
    public static final int SYMBOL_MINUS = 1;
    public static final int SYMBOL_MULTIPLY_4 = 2;
    public static final int SYMBOL_MULTIPLY_5 = 3;
    public static final int SYMBOL_MULTIPLY_6_HOZ = 4;
    public static final int SYMBOL_MULTIPLY_6_VET = 5;
    public static final int SYMBOL_MULTIPLY_DOT = 6;
    public static final int SYMBOL_EQUAL = 7;
    public static final int SYMBOL_EQUAL_3 = 8;
    public static final int SYMBOL_EQUAL_IDENTICAL = 9;
    public static final int SYMBOL_EQUAL_TRIANGLE = 10;
    public static final int SYMBOL_SIMILAR = 11;


    public static final float DEFAULT_SYMBOL_SIZE = 40.0f;

    protected int symbolType;
    protected float symbolSize = DEFAULT_SYMBOL_SIZE;

    public ShownSymbol(int symbolType) {
        this.symbolType = symbolType;
    }

    public void setSymbolType(int symbolType) {
        this.symbolType = symbolType;
    }

    @Override
    public float textSize() {
        return DEFAULT_SYMBOL_SIZE;
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        acc.acc(textSize, 1.0f);
    }

    @Override
    public void assignSize(float textSize) {
        this.symbolSize = textSize;
        measuredRect.width = 0.6f * textSize;
        measuredRect.height = textSize;
        measuredRect.y = -0.5f * textSize;
    }

    @Override
    public float getUpH() {
        return 0.5f * measuredRect.height;
    }

    @Override
    public float getDownH() {
        return 0.5f * measuredRect.height;
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        g.setStroke(StrokePool.getRoundStroke(0.05f * symbolSize));
        float hw = 0.5f * measuredRect.width;
        float eg = 0.12f * measuredRect.width;
        switch (symbolType) {
            case SYMBOL_PLUS:
                g.draw(new Line2D.Float(x + hw, y - hw + eg, x + hw, y + hw - eg));
            case SYMBOL_MINUS:
                paintHoz(g, x + eg, x + measuredRect.width - eg, y);
                break;
            case SYMBOL_MULTIPLY_4:
                paintMulti4(g, x + eg, x + measuredRect.width - eg, y - hw + eg, y + hw - eg);
                break;
            case SYMBOL_MULTIPLY_5:
                paintMulti5(g, x + hw, y, hw - eg);
                break;
            case SYMBOL_MULTIPLY_6_HOZ:
                paintMulti6Hoz(g, x + hw, y, hw - eg);
                break;
            case SYMBOL_MULTIPLY_6_VET:
                paintMulti6Vet(g, x + hw, y, hw);
                break;
            case SYMBOL_MULTIPLY_DOT:
                paintDot(g, x + hw, y, 0.34f * hw);
                break;
            case SYMBOL_EQUAL:
                paintHoz(g, x + eg, x + measuredRect.width - eg, y - 0.35f * hw);
                paintHoz(g, x + eg, x + measuredRect.width - eg, y + 0.35f * hw);
                break;
            case SYMBOL_EQUAL_3:
                paintHoz(g, x + eg, x + measuredRect.width - eg, y - 0.45f * hw);
                paintHoz(g, x + eg, x + measuredRect.width - eg, y);
                paintHoz(g, x + eg, x + measuredRect.width - eg, y + 0.45f * hw);
                break;
            case SYMBOL_EQUAL_IDENTICAL:
                break;
        }
    }

    private void paintHoz(Graphics2D g, float x1, float x2, float y) {
        g.draw(new Line2D.Float(x1, y, x2, y));
    }

    private void paintMulti4(Graphics2D g, float x1, float x2, float y1, float y2) {
        g.draw(new Line2D.Float(x1, y1, x2, y2));
        g.draw(new Line2D.Float(x1, y2, x2, y1));
    }

    private static final float[] MULTI_5_ARRAY = new float[10];
    static {
        for (int i = 0; i < 5; i++) {
            double a = 0.4 * Math.PI * i;
            MULTI_5_ARRAY[i * 2] = (float) Math.sin(a);
            MULTI_5_ARRAY[i * 2 + 1] = (float) Math.cos(a);
        }
    }

    private void paintMulti5(Graphics2D g, float xc, float yc, float r) {
        for (int i = 0; i < 5; i++) {
            g.draw(new Line2D.Float(xc, yc, xc + r * MULTI_5_ARRAY[i * 2], yc - r * MULTI_5_ARRAY[i * 2 + 1]));
        }
    }

    private static final float MULTI_6_COF = (float) (0.5 * Math.sqrt(3));

    private void paintMulti6Hoz(Graphics2D g, float xc, float yc, float r) {
        g.draw(new Line2D.Float(xc - r, yc, xc + r, yc));
        g.draw(new Line2D.Float(xc - 0.5f * r, yc - MULTI_6_COF * r, xc + 0.5f * r, yc + MULTI_6_COF * r));
        g.draw(new Line2D.Float(xc - 0.5f * r, yc + MULTI_6_COF * r, xc + 0.5f * r, yc - MULTI_6_COF * r));
    }

    private void paintMulti6Vet(Graphics2D g, float xc, float yc, float r) {
        g.draw(new Line2D.Float(xc, yc - r, xc, yc + r));
        g.draw(new Line2D.Float(xc - MULTI_6_COF * r, yc - 0.5f * r, xc + MULTI_6_COF * r, yc + 0.5f * r));
        g.draw(new Line2D.Float(xc - MULTI_6_COF * r, yc + 0.5f * r, xc + MULTI_6_COF * r, yc - 0.5f * r));
    }

    private void paintDot(Graphics2D g, float x, float y, float r) {
        g.fill(new Ellipse2D.Float(x - r, y - r, 2 * r, 2 * r));
    }
}
