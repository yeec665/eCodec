package indi.hiro.common.math.basic;

public interface BoundI extends SetI {

    static void checkPositive(int x) {
        if (x <= 0) {
            throw new IllegalArgumentException();
        }
    }

    static void checkNonNegative(int x) {
        if (x < 0) {
            throw new IllegalArgumentException();
        }
    }

    static int min(int a, int b) {
        return a < b ? a : b;
    }

    static int min(int a, int b, int c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    static int min(int a, int b, int c, int d) {
        return min(min(a, b), min(c, d));
    }

    static int max(int a, int b) {
        return a > b ? a : b;
    }

    static int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    static int max(int a, int b, int c, int d) {
        return max(max(a, b), max(c, d));
    }

    int lowerBound();

    int upperBound();

    int confine(int x);

    @Override
    default int size() {
        return upperBound() - lowerBound() + 1;
    }

    @Override
    default int get(int i) {
        if (i >= 0 && i <= upperBound() - lowerBound()) {
            return lowerBound() + i;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    default BoundI bound() {
        return this;
    }

    default boolean in(int[] array, int n) {
        for (int i = 0; i < n; i++) {
            if (!in(array[i])) {
                return false;
            }
        }
        return true;
    }

    default void checkArgument(int x) {
        if (!in(x)) {
            throw new IllegalArgumentException();
        }
    }

    default void checkArgument(int[] array, int n) {
        if (!in(array, n)) {
            throw new IllegalArgumentException();
        }
    }
}
