package indi.hiro.common.math.basic;

public interface BoundF {

    static void checkPositive(float x) {
        if (!(x > 0)) {
            throw new IllegalArgumentException();
        }
    }

    static void checkNonNegative(float x) {
        if (!(x >= 0)) {
            throw new IllegalArgumentException();
        }
    }

    static void check01(float x) {
        if (!(x >= 0 && x <= 1)) {
            throw new IllegalArgumentException();
        }
    }

    static float min(float a, float b) {
        return a < b ? a : b;
    }

    static float min(float a, float b, float c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    static float min(float a, float b, float c, float d) {
        return min(min(a, b), min(c, d));
    }

    static float max(float a, float b) {
        return a > b ? a : b;
    }

    static float max(float a, float b, float c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    static float max(float a, float b, float c, float d) {
        return max(max(a, b), max(c, d));
    }

    static float absMax(float a, float b) {
        return Math.abs(a) > Math.abs(b) ? a : b;
    }

    static float absMax(float a, float b, float c) {
        float absA = Math.abs(a);
        float absB = Math.abs(b);
        float absC = Math.abs(c);
        return absA > absB ? (absA > absC ? a : c) : (absB > absC ? b : c);
    }

    static float absMax(float a, float b, float c, float d) {
        return absMax(absMax(a, b), absMax(c, d));
    }

    float lowerBound();

    float upperBound();

    float length();

    boolean in(float x);

    float confine(float x);

    default boolean in(float[] array, int n) {
        for (int i = 0; i < n; i++) {
            if (!in(array[i])) {
                return false;
            }
        }
        return true;
    }

    default void checkArgument(float x) {
        if (!in(x)) {
            throw new IllegalArgumentException();
        }
    }

    default void checkArgument(float[] array, int n) {
        if (!in(array, n)) {
            throw new IllegalArgumentException();
        }
    }
}
