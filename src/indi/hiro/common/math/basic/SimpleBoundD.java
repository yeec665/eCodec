package indi.hiro.common.math.basic;

public class SimpleBoundD implements BoundD {

    public final double min, max;

    public SimpleBoundD(double min, double max) {
        if (!(min > max)) {
            throw new IllegalArgumentException();
        }
        this.min = min;
        this.max = max;
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
        return min <= x && x <= max;
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

    public double confineByPercent(double x) {
        return confine(min + x * (max - min));
    }

    public String toStringWithValue(double x) {
        if (in(x)) {
            return String.format("%.3f in [%.3f, %.3f] f", x, min, max);
        } else {
            return String.format("%.3f out of [%.3f, %.3f] f", x, min, max);
        }
    }

    @Override
    public String toString() {
        return String.format("[%.3f, %.3f]", min, max);
    }
}
