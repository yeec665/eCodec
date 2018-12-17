package indi.hiro.common.lang;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class LgPaintBuilder {

    private static final ThreadLocal<LgPaintBuilder> BUILDER = ThreadLocal.withInitial(LgPaintBuilder::new);

    private static final float[] F_0_1 = new float[]{0.0f, 1.0f};

    public static LinearGradientPaint buildHorizontal(float x1, float x2, int c1, int c2) {
        return buildHorizontal(x1, x2, new Color(c1), new Color(c2));
    }

    public static LinearGradientPaint buildHorizontal(float x1, float x2, Color c1, Color c2) {
        return new LinearGradientPaint(x1, 0, x2, 0,
                F_0_1, new Color[]{c1, c2});
    }

    public static LinearGradientPaint buildVertical(float y1, float y2, Color c1, Color c2) {
        return new LinearGradientPaint(0, y1, 0, y2,
                F_0_1, new Color[]{c1, c2});
    }

    public final Point2D.Float p1 = new Point2D.Float();
    public final Point2D.Float p2 = new Point2D.Float();
    private final AffineTransform identityTransform = new AffineTransform();
    private final Color[] colors = new Color[2];

    public LgPaintBuilder() {

    }

    public Color getColor1() {
        return colors[0];
    }

    public void setColor1(Color c) {
        colors[0] = c;
    }

    public Color getColor2() {
        return colors[1];
    }

    public void setColor2(Color c) {
        colors[1] = c;
    }

    public LinearGradientPaint build() {
        return new LinearGradientPaint(p1, p2, F_0_1, colors,
                MultipleGradientPaint.CycleMethod.NO_CYCLE,
                MultipleGradientPaint.ColorSpaceType.LINEAR_RGB,
                identityTransform);
    }
}
