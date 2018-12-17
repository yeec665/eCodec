package indi.hiro.common.math.sym.nBasic;

public class IntUtil {

    public static final IntN DEFAULT_M_ONE = BcdInt.BCD_M_ONE;
    public static final IntN DEFAULT_ZERO = BcdInt.BCD_ZERO;
    public static final IntN DEFAULT_ONE = BcdInt.BCD_ONE;
    public static final IntN DEFAULT_TWO = BcdInt.BCD_TWO;

    public static final long I_MASK            = 0x00000000FFFFFFFFL;
    public static final long INT_UNSIGNED_OV   = 0x0000000100000000L;

    public static final double IUO_D = (double) INT_UNSIGNED_OV;

    public static void checkRadix(int radix) {
        if (radix < 2 || radix > 36) {
            throw new IllegalArgumentException();
        }
    }

    public static boolean inClosedInterval(int a, int x, int b) {
        return a <= x && x <= b;
    }

    public static boolean validDecDigit(int v) {
        return 0 <= v && v < 10;
    }

    public static int min(int a, int b) {
        return a > b ? b : a;
    }

    public static int min(int a, int b, int c) {
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    public static int min(int a, int b, int c, int d) {
        return min(min(a, b), min(c, d));
    }

    public static int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static int confine(int a, int b, int c) {
        return b < a ? a : (b > c ? c : b);
    }

    public static int cIndex(int x, int len) {
        return x < len ? (x < 0 ? 0 : x) : len - 1;
    }

    public static int cIndex(double x, int len) {
        int ix = (int) x;
        return ix < len ? (ix < 0 ? 0 : ix) : len - 1;
    }

    public static int ceilDivide(int a, int b) {
        return (a + b - 1) / b;
    }

    public static int positiveMod(int a, int b) {
        a %= b;
        if (a < 0) {
            a += b;
        }
        return a;
    }

    public static void appendHexTo(StringBuilder sb, int v) {
        for (int i = 7; i >= 0; i--) {
            int vHex = 0xF & (v >>> (i << 2));
            if (vHex < 0xA) {
                sb.append((char) ('0' + vHex));
            } else {
                sb.append((char) ('A' - 0xA + vHex));
            }
        }
    }

    public static String intToHex(String head, int v) {
        StringBuilder sb = new StringBuilder(head);
        appendHexTo(sb, v);
        return sb.toString();
    }

    public static String intToHex(String head, String div, int v) {
        StringBuilder sb = new StringBuilder(head);
        sb.append(div);
        appendHexTo(sb, v);
        return sb.toString();
    }

    public static boolean isZero(int[] mag, int len) {
        for (int i = len - 1; i >= 0; i--) {
            if (mag[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private static void bitShiftShort(int src, int[] dst, int bitLen, int bitMov) {
        int start = bitMov >> 5;
        int finish = (bitLen + bitMov) >> 5;
        if (start == finish) {
            src &= 0;
        } else {
            int off = bitMov & 0x1F;
            dst[start] |= 0xFFFFFFFF << off;
            dst[start] &= src << off;
            off = 32 - (bitLen + bitMov) & 0x1F;
            dst[finish] |= 0xFFFFFFFF >>> off;
            dst[finish] &= src >>> off;
        }
    }

    private static void bitShiftLong(int[] src, int[] dst, int bitLen, int bitMov) {
        int start = bitMov >> 5;
        int finish = (bitLen + bitMov) >> 5;
        int offset = bitMov & 0x1F;
        int cpOffset = 32 - bitMov;
        dst[start] |= 0xFFFFFFFF << offset;
        dst[start] &= src[0] << offset;
        int j = 0;
        for (int i = start + 1; i < finish; i++) {
            int x = (0xFFFFFFFF >>> cpOffset) & (src[j++] >>> cpOffset);
            dst[start] = x | ((0xFFFFFFFF << offset) & (src[j] << offset));
        }
        cpOffset = 32 - (bitLen + bitMov) & 0x1F;
        dst[finish] |= 0xFFFFFFFF >>> cpOffset;
        dst[finish] &= src[j] >>> cpOffset;
    }

    /**
     * Move into empty array
     */
    public static void bitShiftL(int[] src, int[] dst, int srcLen, int bitMov) {
        if (bitMov < 0) {
            throw new IllegalArgumentException();
        }
        int intMov = bitMov >> 5;
        bitMov -= intMov << 5;
        if (bitMov == 0) {
            System.arraycopy(src, 0, dst, intMov, srcLen);
        } else {
            int ptr = 0;
            int cpBitMov = 32 - bitMov;
            dst[intMov + ptr] = src[ptr] << bitMov;
            while (++ptr < srcLen) {
                dst[intMov + ptr] = (src[ptr] << bitMov) | (src[ptr - 1] >>> cpBitMov);
            }
            dst[intMov + ptr] = src[ptr - 1] >>> cpBitMov;
        }
    }

    /**
     * Move 1 bit right
     */
    public static void bitShiftR(int[] array, int len) {
        len--;
        for (int i = 0; i < len; i++) {
            array[i] >>>= 1;
            if ((array[i + 1] & 1) != 0) {
                array[i] |= Integer.MIN_VALUE;
            }
        }
        array[len] >>>= 1;
    }
}
