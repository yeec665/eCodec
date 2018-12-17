package indi.hiro.common.math.basic;

public interface BoundL {

    static long min(long a, long b) {
        return a < b ? a : b;
    }

    static long min(long a, long b, long c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    static long min(long a, long b, long c, long d) {
        return min(min(a, b), min(c, d));
    }

    static long max(long a, long b) {
        return a > b ? a : b;
    }

    static long max(long a, long b, long c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    static long max(long a, long b, long c, long d) {
        return max(max(a, b), max(c, d));
    }
}
