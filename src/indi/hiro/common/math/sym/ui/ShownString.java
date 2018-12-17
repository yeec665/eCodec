package indi.hiro.common.math.sym.ui;

import java.awt.*;

public class ShownString extends ShownOp {

    public static final float DEFAULT_TEXT_SIZE = 40.0f;

    protected String content = " ";
    protected float textSize = DEFAULT_TEXT_SIZE;

    public ShownString() {

    }

    public ShownString(String content) {
        this.content = content;
    }

    public void catL(String s) {
        content = s + content;
    }

    public void catR(String s) {
        content = content + s;
    }

    @Override
    public float textSize() {
        return DEFAULT_TEXT_SIZE;
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        acc.acc(textSize, content.length());
    }

    protected void cutRect(float r) {
        r *= measuredRect.height;
        measuredRect.y += r;
        measuredRect.height -= 2 * r;
    }

    @Override
    public void assignSize(float textSize) {
        this.textSize = textSize;
        updateRect(FontPool.getPlain(textSize).getStringBounds(content, FontPool.FRC));
        cutRect(0.136f);
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
        g.setFont(FontPool.getPlain(textSize));
        g.drawString(content, x, y - 0.5f * measuredRect.height - measuredRect.y);
    }
}
