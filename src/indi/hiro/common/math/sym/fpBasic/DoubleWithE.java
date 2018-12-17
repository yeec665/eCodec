package indi.hiro.common.math.sym.fpBasic;

public class DoubleWithE {

    public static double square(double x) {
        return x * x;
    }

    public static double inc(double x) {
        return Math.nextAfter(x, Double.POSITIVE_INFINITY);
    }

    public static DoubleWithE sum(DoubleWithE a, DoubleWithE b) {
        return new DoubleWithE(a.v + b.v, inc(Math.sqrt(square(a.v) + square(b.v))));
    }

    public static DoubleWithE sum(DoubleWithE[] array) {
        double vSum = 0;
        double eeSum = 0;
        for (DoubleWithE dwe : array) {
            vSum += dwe.v;
            eeSum += square(dwe.e);
        }
        return new DoubleWithE(vSum, inc(Math.sqrt(eeSum)));
    }

    public static DoubleWithE difference(DoubleWithE a, DoubleWithE b) {
        return new DoubleWithE(a.v - b.v, inc(Math.sqrt(square(a.v) + square(b.v))));
    }

    public static DoubleWithE product(DoubleWithE a, DoubleWithE b) {
        return new DoubleWithE(a.v * b.v, inc(Math.sqrt(square(a.v * b.e) + square(a.e * b.v))));
    }

    public static DoubleWithE product(DoubleWithE[] array) {
        double vProduct = 0;
        double eeSum = 0;
        for (DoubleWithE dwe : array) {
            vProduct *= dwe.v;
        }
        for (DoubleWithE dwe : array) {
            eeSum += square(vProduct * dwe.e / dwe.v);
        }
        return new DoubleWithE(vProduct, inc(Math.sqrt(eeSum)));
    }

    /**
     * n >= 1
     */
    public static double power(double x, int n) {
        if (n > 10) {
            return powerBin(x, n);
        }
        double out = x;
        while (--n > 0) {
            out *= x;
        }
        return out;
    }

    /**
     * n >= 0
     */
    public static double powerBin(double x, int n) {
        double out = 1.0;
        for (int i = 31 - Integer.numberOfLeadingZeros(n); i >= 0; i--) {
            if ((n & (1 << i)) != 0) {
                out *= x;
            }
            if (i > 0) {
                out *= out;
            }
        }
        return out;
    }

    public static DoubleWithE power(DoubleWithE x, int n) {
        if (n > 1) {
            return new DoubleWithE(power(x.v, n), inc(n * power(x.v, n - 1) * x.e));
        } else if (n == 1) {
            return x;
        } else if (n == 0) {
            return new DoubleWithE(1.0, 0.0);
        }
        throw new IllegalArgumentException();
    }

    private static double defaultError(double v) {
        if (Double.isInfinite(v)) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.nextAfter(v, Double.POSITIVE_INFINITY) - v;
    }

    public final double v, e;

    public DoubleWithE(double v, double e) {
        this.v = v;
        this.e = e;
    }

    public DoubleWithE(double v) {
        this(v, defaultError(v));
    }

    public boolean overlap(DoubleWithE dwe) {
        return Math.abs(v - dwe.v) <= e + dwe.e;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DoubleWithE && v == ((DoubleWithE) obj).v && e == ((DoubleWithE) obj).e;
    }

    @Override
    public String toString() {
        return "DoubleWithE[" + v + "±" + e + "]";
    }
}
