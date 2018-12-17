package indi.hiro.common.math.sym.ui;

import java.awt.geom.Path2D;

public class PathBuilderF {

    private Path2D.Float path;

    public PathBuilderF() {
        path = new Path2D.Float(Path2D.WIND_NON_ZERO);
    }

    public PathBuilderF moveTo(float x, float y) {
        path.moveTo(x, y);
        return this;
    }

    public PathBuilderF lineTo(float x, float y) {
        path.lineTo(x, y);
        return this;
    }

    public PathBuilderF quadTo(float x1, float y1, float x2, float y2) {
        path.quadTo(x1, y1, x2, y2);
        return this;
    }

    public PathBuilderF cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        path.curveTo(x1, y1, x2, y2, x3, y3);
        return this;
    }

    public PathBuilderF close() {
        path.closePath();
        return this;
    }

    public Path2D.Float closeAndFinish() {
        path.closePath();
        return path;
    }

    public Path2D.Float finish() {
        return path;
    }
}
