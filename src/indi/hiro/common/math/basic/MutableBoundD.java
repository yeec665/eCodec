package indi.hiro.common.math.basic;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MutableBoundD implements BoundD {

    public static Rectangle orthogonalBuildI(MutableBoundD x, MutableBoundD y) {
        return new Rectangle((int) x.min, (int) y.min, (int) (x.max - x.min), (int) (y.max - y.min));
    }

    public static Rectangle orthogonalBuildI(double[] coords) {
        MutableBoundD boundX = new MutableBoundD();
        MutableBoundD boundY = new MutableBoundD();
        for (int i = 0; i < coords.length;) {
            boundX.include(coords[i++]);
            boundY.include(coords[i++]);
        }
        return orthogonalBuildI(boundX, boundY);
    }

    public static Rectangle2D.Double orthogonalBuildD(MutableBoundD x, MutableBoundD y) {
        return new Rectangle2D.Double(x.min, y.min, x.max - x.min, y.max - y.min);
    }

    public static Rectangle2D.Double orthogonalBuildD(double[] coords) {
        MutableBoundD boundX = new MutableBoundD();
        MutableBoundD boundY = new MutableBoundD();
        for (int i = 0; i < coords.length;) {
            boundX.include(coords[i++]);
            boundY.include(coords[i++]);
        }
        return orthogonalBuildD(boundX, boundY);
    }

    private double min, max;

    public MutableBoundD() {
        reset();
    }

    public void reset() {
        reset(Double.NaN);
    }

    public void reset(double x) {
        min = x;
        max = x;
    }

    public void include(double x) {
        if (!(x > min)) {
            min = x;
        }
        if (!(x < max)) {
            max = x;
        }
    }

    @Override
    public double lowerBound() {
        return min;
    }

    @Override
    public double upperBound() {
        return max;
    }

    @Override
    public double length() {
        return max - min;
    }

    @Override
    public boolean in(double x) {
        return x >= min && x <= max;
    }

    @Override
    public double confine(double x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }
}
