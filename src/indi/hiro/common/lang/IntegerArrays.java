package indi.hiro.common.lang;

import java.util.function.IntPredicate;

public class IntegerArrays {

    public static final int[] ZL_ARRAY = new int[0];

    public static void greedyCopy(int[] src, int srcPos, int[] dest, int destPos) {
        int len = Math.min(src.length - srcPos, dest.length - destPos);
        if (len > 0) {
            System.arraycopy(src, srcPos, dest, destPos, len);
        }
    }

    public static int[] concat(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static int[] copy1(int[] original) {
        int n = original.length;
        int[] replica = new int[n];
        System.arraycopy(original, 0, replica, 0, n);
        return replica;
    }

    public static int[][] copy2(int[][] original) {
        int n = original.length;
        int[][] replica = new int[n][];
        for (int i = 0; i < n; i++) {
            replica[i] = copy1(original[i]);
        }
        return replica;
    }

    public static int[] copyRepeat(int[] src, int n) {
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
            int[] dst = new int[dstLen];
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

    public static void fillRepeat(int[] src, int srcOff, int srcLen, int[] dst, int dstOff, int n) {
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

    public static int[] arithmeticSequence(int len, int start, int diff) {
        int[] ii = new int[len];
        if (diff != 0) {
            for (int i = 0; i < len; i++) {
                ii[i] = start;
                start += diff;
            }
        } else if (start != 0) {
            for (int i = 0; i < len; i++) {
                ii[i] = start;
            }
        }
        return ii;
    }

    public static int sum(int[] ii) {
        int sum = 0;
        for (int i : ii) {
            sum += i;
        }
        return sum;
    }

    public static int ifCount(int[] ii, IntPredicate predicate) {
        int n = 0;
        for (int i : ii){
            if (predicate.test(i)) {
                n++;
            }
        }
        return n;
    }

    public static int ifSum(int[] ii, IntPredicate predicate) {
        int sum = 0;
        for (int i : ii){
            if (predicate.test(i)) {
                sum += i;
            }
        }
        return sum;
    }

    public static boolean increasing(int[] ii) {
        int v = Integer.MIN_VALUE;
        for (int i : ii) {
            if (v > i) {
                return false;
            }
            v = i;
        }
        return true;
    }

    public static boolean strictlyIncreasing(int[] ii) {
        for (int i = 1; i < ii.length; i++) {
            if (ii[i - 1] >= ii[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean decreasing(int[] ii) {
        int v = Integer.MAX_VALUE;
        for (int i : ii) {
            if (v < i) {
                return false;
            }
            v = i;
        }
        return true;
    }

    public static boolean strictlyDecreasing(int[] ii) {
        for (int i = 1; i < ii.length; i++) {
            if (ii[i - 1] <= ii[i]) {
                return false;
            }
        }
        return true;
    }

    public static int logicAnd(int[] ii) {
        int sum = -1;
        for (int i : ii) {
            sum &= i;
        }
        return sum;
    }

    public static int logicOr(int[] ii) {
        int sum = 0;
        for (int i : ii) {
            sum |= i;
        }
        return sum;
    }

    public static String toString(int[] ii) {
        StringBuilder sb = new StringBuilder("[");
        append(sb, ii);
        return sb.append("]").toString();
    }

    public static void append(StringBuilder sb, int[] ii) {
        for (int i = 0; i < ii.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(ii[i]);
        }
    }
}