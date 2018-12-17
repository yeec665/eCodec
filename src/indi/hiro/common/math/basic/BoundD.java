package indi.hiro.common.math.basic;

public interface BoundD {

    static void checkPositive(double x) {
        if (!(x > 0)) {
            throw new IllegalArgumentException();
        }
    }

    static int looseCompare(double a, double b) {
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return 0;
    }

    static double min(double a, double b) {
        return a < b ? a : b;
    }

    static double min(double a, double b, double c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    static double min(double a, double b, double c, double d) {
        return min(min(a, b), min(c, d));
    }

    static double max(double a, double b) {
        return a > b ? a : b;
    }

    static double max(double a, double b, double c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    static double max(double a, double b, double c, double d) {
        return max(max(a, b), max(c, d));
    }

    static double absMax(double a, double b) {
        return Math.abs(a) > Math.abs(b) ? a : b;
    }

    static double absMax(double a, double b, double c) {
        double absA = Math.abs(a);
        double absB = Math.abs(b);
        double absC = Math.abs(c);
        return absA > absB ? (absA > absC ? a : c) : (absB > absC ? b : c);
    }

    static double absMax(double a, double b, double c, double d) {
        return absMax(absMax(a, b), absMax(c, d));
    }

    double lowerBound();

    double upperBound();

    double length();

    boolean in(double x);

    double confine(double x);

    default boolean in(double[] array, int n) {
        for (int i = 0; i < n; i++) {
            if (!in(array[i])) {
                return false;
            }
        }
        return true;
    }

    default void checkArgument(double x) {
        if (!in(x)) {
            throw new IllegalArgumentException();
        }
    }

    default void checkArgument(double[] array, int n) {
        if (!in(array, n)) {
            throw new IllegalArgumentException();
        }
    }
}
