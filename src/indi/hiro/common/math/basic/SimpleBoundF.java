package indi.hiro.common.math.basic;

public class SimpleBoundF implements BoundF, java.io.Serializable {

    private static final long serialVersionUID = 0xAB936B860926793BL;

    public final float min, max;

    public SimpleBoundF(float min, float max) {
        if (!(min <= max)) {
            throw new IllegalArgumentException();
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public float lowerBound() {
        return min;
    }

    @Override
    public float upperBound() {
        return max;
    }

    @Override
    public float length() {
        return max - min;
    }

    @Override
    public boolean in(float x) {
        return x >= min && x <= max;
    }

    @Override
    public float confine(float x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    public float confineByPercent(float x) {
        return confine(min + x * (max - min));
    }

    public float confineByPercent(double x) {
        return confineByPercent((float) x);
    }

    public String toStringWithValue(float x) {
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
