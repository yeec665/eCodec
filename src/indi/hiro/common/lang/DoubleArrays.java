package indi.hiro.common.lang;

import java.util.function.DoublePredicate;

public class DoubleArrays {

    public static final double[] ZL_ARRAY = new double[0];

    public static void greedyCopy(double[] src, int srcPos, double[] dest, int destPos) {
        int len = Math.min(src.length - srcPos, dest.length - destPos);
        if (len > 0) {
            System.arraycopy(src, srcPos, dest, destPos, len);
        }
    }

    public static double[] copy1(double[] original) {
        int n = original.length;
        double[] replica = new double[n];
        System.arraycopy(original, 0, replica, 0, n);
        return replica;
    }

    public static double[][] copy2(double[][] original) {
        int n = original.length;
        double[][] replica = new double[n][];
        for (int i = 0; i < n; i++) {
            replica[i] = copy1(original[i]);
        }
        return replica;
    }

    public static double[] copyRepeat(double[] src, int n) {
        if (n <= 1) {
            if (n == 1) {
                return copy1(src);
            } else {
                return ZL_ARRAY;
            }
        } else {
            int srcLen = src.length;
            if (srcLen == 0) {
                return ZL_ARRAY;
            }
            int dstLen = n * srcLen;
            int lim = dstLen / 2;
            double[] dst = new double[dstLen];
            System.arraycopy(src, 0, dst, 0, srcLen);
            do {
                System.arraycopy(dst, 0, dst, srcLen, srcLen);
                srcLen *= 2;
            } while (srcLen <= lim);
            if (srcLen < dstLen) {
                lim = dstLen - srcLen;
                System.arraycopy(dst, 0, dst, srcLen, lim);
            }
            return dst;
        }
    }

    public static void fillRepeat(double[] src, int srcOff, int srcLen, double[] dst, int dstOff, int n) {
        if (srcOff < 0 || src.length < srcOff + srcLen) {
            throw new IndexOutOfBoundsException();
        }
        if (srcLen <= 0) {
            if (srcLen == 0) {
                return;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
        if (n <= 1) {
            if (n == 1) {
                System.arraycopy(src, srcOff, dst, dstOff, srcLen);
            }
            return;
        }
        int dstLen = srcLen * n;
        if (dstOff < 0 || dst.length < dstOff + dstLen) {
            throw new IndexOutOfBoundsException();
        }
        int lim = dstLen / 2;
        System.arraycopy(src, srcOff, dst, dstOff, srcLen);
        do {
            System.arraycopy(dst, dstOff, dst, dstOff + srcLen, srcLen);
            srcLen *= 2;
        } while (srcLen <= lim);
        if (srcLen < dstLen) {
            lim = dstLen - srcLen;
            System.arraycopy(dst, dstOff, dst, dstOff + srcLen, lim);
        }
    }

    public static double[] concat(double[] a, double[] b) {
        double[] c = new double[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static double[] linspace(int len, double from, double to) {
        double[] dd = new double[len];
        for (int i = 0; i < len; i++) {
            double m = i / (len - 1);
            dd[i] = (1.0 - m) * from + m * to;
        }
        return dd;
    }

    public static double[] arithmeticSequence(int len, double start, double diff) {
        double[] dd = new double[len];
        for (int i = 0; i < len; i++) {
            dd[i] = start;
            start += diff;
        }
        return dd;
    }

    public static double[] geometricSequence(int len, double start, double ratio) {
        double[] dd = new double[len];
        for (int i = 0; i < len; i++) {
            dd[i] = start;
            start *= ratio;
        }
        return dd;
    }

    public static double sum(double[] dd) {
        double sum = 0;
        for (double x : dd) {
            sum += x;
        }
        return sum;
    }

    public static double deciSum(double[] dd, int start, int decimation) {
        double sum = 0;
        for (int i = start; i < dd.length; i += decimation) {
            sum += dd[i];
        }
        return sum;
    }

    public static double ifSum(double[] dd, DoublePredicate predicate) {
        double sum = 0;
        for (double x : dd) {
            if (predicate.test(x)) {
                sum += x;
            }
        }
        return sum;
    }

    public static double average(double[] dd) {
        double sum = 0;
        for (double x : dd) {
            sum += x;
        }
        return sum / dd.length;
    }

    public static double deciAverage(double[] dd, int start, int decimation) {
        double sum = 0;
        int n = 0;
        for (int i = start; i < dd.length; i += decimation) {
            sum += dd[i];
            n++;
        }
        return sum / n;
    }

    public static double ifAverage(double[] dd, DoublePredicate predicate) {
        double sum = 0;
        int n = 0;
        for (double x : dd) {
            if (predicate.test(x)) {
                sum += x;
                n++;
            }
        }
        return sum;
    }

    public static String toString(double[] dd) {
        StringBuilder sb = new StringBuilder("[");
        append(sb, dd);
        return sb.append("]").toString();
    }

    public static void append(StringBuilder sb, double[] dd) {
        for (int i = 0; i < dd.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format("%.4f", dd[i]));
        }
    }

    public static void coordsTranslate(double[] coords, double x, double y) {
        for (int i = 0, limit = ~1 & coords.length; i < limit;) {
            coords[i++] += x;
            coords[i++] += y;
        }
    }

    public static void coordsTransform(double[] coords, double cos, double sin, double x, double y) {
        for (int ix = 0, iy = 1, limit = coords.length; iy < limit;) {
            double tx = coords[ix];
            double ty = coords[iy];
            coords[ix] = x + cos * tx - sin * ty;
            coords[iy] = y + cos * ty + sin * tx;
            ix += 2;
            iy += 2;
        }
    }

    public static void classTest() {
        double[] d1 = {2.3, 5.1, 0.9};
        for (int i = 1; i < 17; i += 3) {
            System.out.println(FpToStringConverter.arrayToString(copyRepeat(d1, i), 1));
        }
        double[] d2 = new double[50];
        for (int i = 1; i < 22; i += 3) {
            fillRepeat(d1, 1, 2, d2, 2, i);
            System.out.println(FpToStringConverter.arrayToString(d2, 1));
        }
    }
}
