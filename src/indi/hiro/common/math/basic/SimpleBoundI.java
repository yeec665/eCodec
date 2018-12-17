package indi.hiro.common.math.basic;

public class SimpleBoundI implements BoundI {

    public final int min, max;

    public SimpleBoundI(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException();
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public int lowerBound() {
        return min;
    }

    @Override
    public int upperBound() {
        return max;
    }

    @Override
    public boolean in(int x) {
        return x >= min && x <= max;
    }

    @Override
    public int confine(int x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    public int confineByPercent(float x) {
        x = min + x * (max - min);
        return confine(Math.round(x));
    }

    public int confineByPercent(double x) {
        x = min + x * (max - min);
        return confine((int) Math.round(x));
    }
}
