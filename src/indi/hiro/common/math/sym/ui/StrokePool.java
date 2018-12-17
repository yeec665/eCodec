package indi.hiro.common.math.sym.ui;

import java.awt.*;

public class StrokePool {

    private static final int MAX_WIDTH = 40;

    private static final BasicStroke[] SK_RECT_ARRAY = new BasicStroke[MAX_WIDTH];

    private static final BasicStroke[] SK_ROUND_ARRAY = new BasicStroke[MAX_WIDTH];

    public static final BasicStroke SK_RECT_UNIT = getRectStroke(10);

    public static final BasicStroke SK_ROUND_UNIT = getRoundStroke(10);

    public static synchronized BasicStroke getRectStroke(int w) {
        if (w < 1) {
            w = 1;
        }
        if (w > MAX_WIDTH) {
            w = MAX_WIDTH;
        }
        w--;
        if (SK_RECT_ARRAY[w] == null) {
            SK_RECT_ARRAY[w] = new BasicStroke(0.1f * (w + 1), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        }
        return SK_RECT_ARRAY[w];
    }

    public static BasicStroke getRectStroke(float w) {
        return getRectStroke((int) (10 * w + 0.5f));
    }

    public static synchronized BasicStroke getRoundStroke(int w) {
        if (w < 1) {
            w = 1;
        }
        if (w > MAX_WIDTH) {
            w = MAX_WIDTH;
        }
        w--;
        if (SK_ROUND_ARRAY[w] == null) {
            SK_ROUND_ARRAY[w] = new BasicStroke(0.1f * (w + 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }
        return SK_ROUND_ARRAY[w];
    }

    public static BasicStroke getRoundStroke(float w) {
        return getRoundStroke((int) (10 * w + 0.5f));
    }
}
