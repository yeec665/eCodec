package indi.hiro.common.math.sym.ui;

import java.awt.*;

public class ShownMatrix extends ShownOp {

    public static float sum(float[] array) {
        float sum = 0;
        for (float x : array) {
            sum += x;
        }
        return sum;
    }

    public static final float X_GAP = 0.36f, Y_GAP = 0.06f;

    protected final int w, h;
    protected final float[] widths, heightsU, heightsD;
    protected final ShownOp[] contents;

    protected float gapUnit = ShownString.DEFAULT_TEXT_SIZE;

    public ShownMatrix(int w, int h) {
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException();
        }
        this.w = w;
        this.h = h;
        widths = new float[w];
        heightsU = new float[h];
        heightsD = new float[h];
        contents = new ShownOp[w * h];
    }

    public void setSop(ShownOp sop, int x, int y) {
        if (x >= 0 && x < w && y >= 0 && y < h) {
            contents[y * w + x] = sop;
        }
    }

    public ShownOp getSop(int x, int y) {
        if (x >= 0 && x < w && y >= 0 && y < h) {
            return contents[y * w + x];
        }
        return null;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void fill(ShownMatrix that) {
        for (int y = Math.min(h, that.h) - 1; y >= 0; y--) {
            System.arraycopy(that.contents, y * that.w, contents, y * w, Math.min(w, that.w));
        }
    }

    @Override
    public float textSize() {
        float ts = 0;
        for (ShownOp sop : contents) {
            if (sop != null) {
                ts = Math.max(sop.textSize(), ts);
            }
        }
        return ts;
    }

    @Override
    public void assignSize(float textSize, TextLevelAcc acc) {
        for (ShownOp sop : contents) {
            if (sop != null) {
                sop.assignSize(textSize, acc);
            }
        }
    }

    @Override
    public void assignSize(float textSize) {
        gapUnit = textSize;
        for (int y = 0; y < h; y++) {
            float upH = 0, downH = 0;
            for (int x = 0; x < w; x++) {
                ShownOp sop = contents[y * w + x];
                if (sop == null) {
                    continue;
                }
                sop.assignSize(textSize);
                widths[x] = Math.max(sop.getWidth(), widths[x]);
                upH = Math.max(sop.getUpH(), upH);
                downH = Math.max(sop.getDownH(), downH);
            }
            heightsU[y] = upH;
            heightsD[y] = downH;
        }
        measuredRect.width = sum(widths) + (w - 1) * gapUnit * X_GAP;
        measuredRect.height = sum(heightsU) + sum(heightsD) + (h - 1) * gapUnit * Y_GAP;
        measuredRect.y = -0.5f * measuredRect.height;
    }

    @Override
    public void paint(Graphics2D g, float x, float y) {
        float ys = y + measuredRect.y;
        for (int iy = 0; iy < h; iy++) {
            ys += heightsU[iy];
            float xs = x;
            for (int ix = 0; ix < w; ix++) {
                ShownOp sop = contents[iy * w + ix];
                if (sop != null) {
                    sop.paint(g, xs + 0.5f * (widths[ix] - sop.getWidth()), ys);
                }
                xs += widths[ix];
                xs += gapUnit * X_GAP;
            }
            ys += heightsD[iy];
            ys += gapUnit * Y_GAP;
        }
    }
}
