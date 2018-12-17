package indi.hiro.common.math.basic;

import java.util.List;

public class IndexBound implements BoundI {

    public static int confine(int i, int len) {
        if (i < 0) {
            i = 0;
        } else if (i >= len) {
            i = len - 1;
        }
        return i;
    }

    public static boolean in(int i, int len) {
        return i >= 0 && i < len;
    }

    public static int confine(float f, int len) {
        return confine((int) f, len);
    }

    public static int confine(double d, int len) {
        return confine((int) d, len);
    }

    public static IndexBound create(Object[] array) {
        return new IndexBound(array.length);
    }

    public static IndexBound create(List<?> list) {
        return new IndexBound(list.size());
    }

    public final int len;

    public IndexBound(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException();
        }
        this.len = len;
    }

    @Override
    public int lowerBound() {
        return 0;
    }

    @Override
    public int upperBound() {
        return len - 1;
    }

    @Override
    public boolean in(int x) {
        return x >= 0 && x < len;
    }

    @Override
    public int confine(int x) {
        if (x < 0) {
            return 0;
        }
        if (x >= len) {
            return len - 1;
        }
        return x;
    }
}
