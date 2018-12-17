package indi.hiro.common.math.sym.ui;

import indi.hiro.common.math.sym.nBasic.MathExtension2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TriangleF implements Shape {

    public static Rectangle convert(float xMin, float xMax, float yMin, float yMax) {
        int x = (int) xMin;
        int y = (int) yMin;
        return new Rectangle(x, y, (int) (xMax + 1) - x, (int) (yMax + 1) - y);
    }

    private final float[] pts = new float[6];

    public TriangleF(float x1, float y1, float x2, float y2, float x3, float y3) {
        pts[0] = x1;
        pts[1] = y1;
        pts[2] = x2;
        pts[3] = y2;
        pts[4] = x3;
        pts[5] = y3;
    }

    @Override
    public Rectangle getBounds() {
        return convert(MathExtension2.min(pts[0], pts[2], pts[4]), MathExtension2.max(pts[0], pts[2], pts[4]),
                MathExtension2.min(pts[1], pts[3], pts[5]), MathExtension2.max(pts[1], pts[3], pts[5]));
    }

    @Override
    public Rectangle2D getBounds2D() {
        float x = MathExtension2.min(pts[0], pts[2], pts[4]);
        float y = MathExtension2.min(pts[1], pts[3], pts[5]);
        return new Rectangle2D.Float(x, y, MathExtension2.max(pts[0], pts[2], pts[4]) - x,
                MathExtension2.max(pts[1], pts[3], pts[5]) - y);
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return getBounds2D().intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return getBounds2D().intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new TriangleIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new TriangleIterator(at);
    }

    private class TriangleIterator implements PathIterator {

        private AffineTransform at;
        private int index;

        TriangleIterator(AffineTransform at) {
            this.at = at != null ? at : FontPool.IDENTITY_AT;
        }

        @Override
        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        @Override
        public boolean isDone() {
            return index > 3;
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (index < 3) {
                at.transform(pts, index * 2, coords, 0, 1);
                return index == 0 ? SEG_MOVETO : SEG_LINETO;
            }
            return SEG_CLOSE;
        }

        @Override
        public int currentSegment(double[] coords) {
            if (index < 3) {
                at.transform(pts, index * 2, coords, 0, 1);
                return index == 0 ? SEG_MOVETO : SEG_LINETO;
            }
            return SEG_CLOSE;
        }
    }
}
