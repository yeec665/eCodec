package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.ds.primitive.BooleanConsumer;
import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownString;
import indi.hiro.common.math.sym.util.SymCorruptedException;

import java.util.Arrays;

public class BinInt extends IntN {

    private static final int POOL_MIN = -128;
    private static final int POOL_MAX = 127;
    private static final int POOL_MASK = 0xFF;
    private static final int POOL_MASK_IS = 0xFFFFFF80;
    private static final BinInt[] POOL = new BinInt[POOL_MAX - POOL_MIN + 1];
    static {
        POOL[0] = new BinInt(new int[0], 0, false);
        for (int i = 1; i <= POOL_MAX; i++) {
            int[] mag = new int[]{i};
            POOL[i] = new BinInt(mag, 1, false);
            POOL[POOL_MASK & -i] = new BinInt(mag, 1, true);
        }
        POOL[POOL_MASK & POOL_MIN] = new BinInt(new int[]{128}, 1, true);
    }
    public static final BinInt BIN_ZERO = POOL[0];
    public static final BinInt BIN_ONE = POOL[1];
    public static final BinInt BIN_M_ONE = POOL[POOL_MASK];

    public static BinInt create(int v) {
        if (POOL_MIN <= v && v <= POOL_MAX) {
            return POOL[POOL_MASK & v];
        }
        return new BinInt(new int[]{Math.abs(v)}, v < 0);
    }

    public static BinInt create(int[] mag, boolean sign) {
        int len = mag.length;
        while (len > 0 && mag[len - 1] == 0) {
            len--;
        }
        if (len == 0) {
            return BIN_ZERO;
        } else if (len == 1 && (POOL_MASK_IS & mag[0]) == 0) {
            int index = POOL_MASK & (sign ? -mag[0] : mag[0]);
            return POOL[index];
        } else {
            return new BinInt(mag, len, sign);
        }
    }

    private static int compare(int[] a, int[] b, int len) {
        for (int i = len - 1; i >= 0; i--) {
            if (a[i] != b[i]) {
                return (IntUtil.I_MASK & a[i]) > (IntUtil.I_MASK & b[i]) ? 1 : -1;
            }
        }
        return 0;
    }

    private static int compare(int[] a, int aLen, int[] b, int bLen) {
        if (aLen != bLen) {
            return Integer.compare(aLen, bLen);
        } else {
            return compare(a, b, aLen);
        }
    }

    public static int compare(BinInt a, BinInt b) {
        if (a.sign != b.sign) {
            return a.sign ? -1 : 1;
        } else {
            int v = compare(a.mag, a.len, b.mag, b.len);
            return a.sign ? -v : v;
        }
    }

    private static int[] plus(int[] a, int aLen, int[] b, int bLen) {
        if (aLen < bLen) {
            int[] t = a;
            a = b;
            b = t;
            int tLen = aLen;
            aLen = bLen;
            bLen = tLen;
        }
        int[] sum;
        if (aLen == bLen && (Integer.MIN_VALUE & (a[aLen - 1] | b[bLen - 1])) != 0
                || (Integer.MIN_VALUE & a[aLen - 1]) != 0) {
            sum = new int[aLen + 1];
        } else {
            sum = new int[aLen];
        }
        long v = 0;
        for (int i = 0; i < aLen; i++) {
            v += IntUtil.I_MASK & a[i];
            if (i < bLen) {
                v += IntUtil.I_MASK & b[i];
            }
            sum[i] = (int) v;
            v >>>= 32;
        }
        if (aLen < sum.length) {
            sum[aLen] = (int) v;
        }
        return sum;
    }

    private static void minus(int[] a, int[] b, int len) {
        long v = 0;
        for (int i = 0; i < len; i++) {
            v += IntUtil.I_MASK & a[i];
            v -= IntUtil.I_MASK & b[i];
            a[i] = (int) v;
            v >>= 32;
        }
    }

    private static int[] minus(int[] a, int aLen, int[] b, int bLen) {
        int[] sum = new int[aLen];
        long v = 0;
        for (int i = 0; i < aLen; i++) {
            v += IntUtil.I_MASK & a[i];
            if (i < bLen) {
                v -= IntUtil.I_MASK & b[i];
            }
            sum[i] = (int) v;
            v >>= 32;
        }
        return sum;
    }

    public static BinInt plus(BinInt a, BinInt b) {
        if (a.sign == b.sign) {
            return create(plus(a.mag, a.len, b.mag, b.len), a.sign);
        } else {
            int v = compare(a.mag, a.len, b.mag, b.len);
            if (v == 0) {
                return BIN_ZERO;
            } else if (v > 0) {
                return create(minus(a.mag, a.len, b.mag, b.len), a.sign);
            } else {
                return create(minus(b.mag, b.len, a.mag, a.len), b.sign);
            }
        }
    }

    public static BinInt minus(BinInt a, BinInt b) {
        if (a.sign != b.sign) {
            return create(plus(a.mag, a.len, b.mag, b.len), a.sign);
        } else {
            int v = compare(a.mag, a.len, b.mag, b.len);
            if (v == 0) {
                return BIN_ZERO;
            } else if (v > 0) {
                return create(minus(a.mag, a.len, b.mag, b.len), a.sign);
            } else {
                return create(minus(b.mag, b.len, a.mag, a.len), !b.sign);
            }
        }
    }

    private static void plusTo(int[] x, long d, int mov) {
        while (d != 0) {
            d += (IntUtil.I_MASK & x[mov]);
            x[mov] = (int) d;
            d >>>= 32;
            mov++;
        }
    }

    private static int[] multiply(int[] a, int aLen, int[] b, int bLen) {
        if (aLen == 1 && bLen == 1 && (a[0] & 0xFFFF0000) == 0 && (b[0] & 0xFFFF0000) == 0) {
            return new int[]{a[0] * b[0]};
        }
        if (aLen < bLen) {
            int[] t = a;
            a = b;
            b = t;
            int tLen = aLen;
            aLen = bLen;
            bLen = tLen;
        }
        int[] product = new int[aLen + bLen];
        for (int i = 0; i < bLen; i++) {
            for (int j = 0; j < aLen; j++) {
                plusTo(product, (IntUtil.I_MASK & a[j]) * (IntUtil.I_MASK & b[i]), i + j);
            }
        }
        return product;
    }

    public static BinInt multiply(BinInt a, BinInt b) {
        return create(multiply(a.mag, a.len, b.mag, b.len), a.sign ^ b.sign);
    }

    private static IntDivResult<BinInt> innerDivide(BinInt a, BinInt b) {
        int aLen = a.len;
        int[] bShift = new int[aLen + 1];
        int[] remainder = Arrays.copyOf(a.mag, aLen);
        int stepToGo = a.bitLen() - b.bitLen() + 1;
        int[] quotient = new int[(stepToGo + 31) >> 5];
        IntUtil.bitShiftL(b.mag, bShift, b.len, stepToGo - 1);
        while (stepToGo-- > 0) {
            if (compare(bShift, remainder, aLen) < 0) {
                minus(remainder, bShift, aLen);
                quotient[stepToGo >> 5] |= (1 << (stepToGo & 31));
            }
            if (stepToGo != 0) {
                IntUtil.bitShiftR(bShift, aLen);
            }
        }
        return new IntDivResult<>(create(quotient, a.sign ^ b.sign), create(remainder, a.sign));
    }

    private static BinInt innerQuotient(BinInt a, BinInt b) {
        int aLen = a.len;
        int[] bShift = new int[aLen + 1];
        int[] remainder = Arrays.copyOf(a.mag, aLen);
        int stepToGo = a.bitLen() - b.bitLen() + 1;
        int[] quotient = new int[(stepToGo + 31) >> 5];
        IntUtil.bitShiftL(b.mag, bShift, b.len, stepToGo - 1);
        while (stepToGo-- > 0) {
            if (compare(bShift, remainder, aLen) < 0) {
                minus(remainder, bShift, aLen);
                quotient[stepToGo >> 5] |= (1 << (stepToGo & 31));
            }
            if (stepToGo != 0) {
                IntUtil.bitShiftR(bShift, aLen);
            }
        }
        return create(quotient, a.sign ^ b.sign);
    }

    private static BinInt innerRemainder(BinInt a, BinInt b) {
        int aLen = a.len;
        int[] bShift = new int[aLen + 1];
        int[] remainder = Arrays.copyOf(a.mag, aLen);
        int stepToGo = a.bitLen() - b.bitLen() + 1;
        IntUtil.bitShiftL(b.mag, bShift, b.len, stepToGo - 1);
        while (stepToGo-- > 0) {
            if (compare(bShift, remainder, aLen) < 0) {
                minus(remainder, bShift, aLen);
            }
            if (stepToGo != 0) {
                IntUtil.bitShiftR(bShift, aLen);
            }
        }
        return create(remainder, a.sign);
    }

    public static IntDivResult<BinInt> divide(BinInt a, BinInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BinInt divided by zero");
        }
        int v = compare(a.mag, a.len, b.mag, b.len);
        if (v < 0) {
            return new IntDivResult<>(BIN_ZERO, a);
        } else if (v == 0) {
            return new IntDivResult<>((a.sign == b.sign) ? BIN_ONE : BIN_M_ONE, BIN_ZERO);
        } else {
            return innerDivide(a, b);
        }
    }

    public static BinInt quotient(BinInt a, BinInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BinInt divided by zero");
        }
        int v = compare(a.mag, a.len, b.mag, b.len);
        if (v < 0) {
            return BIN_ZERO;
        } else if (v == 0) {
            return (a.sign == b.sign) ? BIN_ONE : BIN_M_ONE;
        } else {
            return innerQuotient(a, b);
        }
    }

    public static BinInt remainder(BinInt a, BinInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BinInt divided by zero");
        }
        int v = compare(a.mag, a.len, b.mag, b.len);
        if (v < 0) {
            return a;
        } else if (v == 0) {
            return BIN_ZERO;
        } else {
            return innerRemainder(a, b);
        }
    }

    private static int divA(int[] mag, int len) {
        long v = 0;
        for (int i = len - 1; i >= 0; i--) {
            v = (v << 32) | (IntUtil.I_MASK & mag[i]);
            mag[i] = (int) (v / 10);
            v %= 10;
        }
        return (int) v;
    }

    private final int[] mag; // from LSB to MSB

    private BinInt(int[] mag, int len, boolean sign) {
        super(len, sign);
        this.mag = mag;
    }

    private BinInt(int[] mag, boolean sign) {
        super(mag.length, sign);
        this.mag = mag;
    }

    // rational();

    // finite();


    @Override
    public BinInt toBinInt() {
        return this;
    }

    @Override
    public BcdInt toBcdInt() {
        byte[] digits = new byte[IntUtil.ceilDivide(bitLen(), 3)];
        int[] tempMag = Arrays.copyOf(mag, len);
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (byte) divA(tempMag, len);
        }
        if (!IntUtil.isZero(tempMag, len)) {
            throw new SymCorruptedException(this);
        }
        return BcdInt.create(digits, sign);
    }

    @Override
    public double dValue() {
        double v = 0;
        for (int i = len - 1; i >= 0; i--) {
            v *= IntUtil.IUO_D;
            v += IntUtil.I_MASK & mag[i];
        }
        return sign ? -v : v;
    }

    // dwe();

    // ceil();

    // floor();

    @Override
    public BinInt complement() {
        if (len == 0) {
            return this;
        }
        return new BinInt(mag, len, !sign);
    }

    // abs();

    @Override
    public IntN myZero() {
        return BIN_ZERO;
    }

    @Override
    public IntN myOne() {
        return BIN_ONE;
    }

    // positive();

    // negative();

    // isZero();

    @Override
    public boolean isOne() {
        return this == BIN_ONE;
    }

    public void appendTo(StringBuilder sb) {
        if (sign) {
            sb.append("-");
        }
        for (int i = len - 1; i >= 0; i--) {
            IntUtil.appendHexTo(sb, mag[i]);
        }
    }

    @Override
    public String represent(int radix) {
        if (radix == 16) {
            StringBuilder sb = new StringBuilder();
            appendTo(sb);
            return sb.toString();
        }
        IntUtil.checkRadix(radix);
        if (hasIntValue()) {
            return Integer.toString(intValue(), radix);
        }
        return BinParser.represent(this, radix);
    }

    @Override
    public void debugToString(StringBuilder sb) {
        if (hasIntValue()) {
            sb.append(intValue());
        }
        BinParser.append(sb, this, 10);
    }

    @Override
    public ShownOp show() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return new ShownString(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BinInt[");
        sb.append(mag.length);
        sb.append(',');
        sb.append(len);
        sb.append(',');
        if (sign) {
            sb.append("-");
        }
        for (int i = len - 1; i >= 0; i--) {
            IntUtil.appendHexTo(sb, mag[i]);
            if (i > 0) {
                sb.append('_');
            }
        }
        return sb.append(']').toString();
    }

    public boolean equalMe(BinInt that) {
        if (this.sign ^ that.sign) {
            return false;
        }
        if (this.len != that.len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (this.mag[i] != that.mag[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof BinInt && equalMe((BinInt) that);
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 4;
        } else if (i == 1) {
            return len;
        } else {
            i -= 2;
            if (i < len) {
                return mag[i];
            } else {
                return -1;
            }
        }
    }

    @Override
    public int orderLength() {
        return 2 + len;
    }

    public int readMag(int index) {
        if (index >= 0 && index < len) {
            return mag[index];
        }
        return 0;
    }

    public int numberOfTrailingZeros() {
        int n = 0;
        for (int i = 0; i < len; i++) {
            int v = mag[i];
            if (v == 0) {
                n += Integer.SIZE;
            } else{
                n += Integer.numberOfTrailingZeros(v);
                break;
            }
        }
        return n;
    }

    private BinInt shiftI(int mov) {
        int newLen = len + mov;
        if (newLen <= 0) {
            return BIN_ZERO;
        }
        int[] newMag = new int[newLen];
        if (mov > 0) {
            // shift left
            System.arraycopy(mag, 0, newMag, mov, len);
        } else {
            // shift right
            System.arraycopy(mag, -mov, newMag, 0, newLen);
        }
        return create(newMag, sign);
    }

    private BinInt shiftII(int mov) {
        int newLen = len;
        if (mov > 0) {
            newLen += (mov + 31) / 32;
        } else {
            newLen += mov / 32;
        }
        if (newLen <= 0) {
            return BIN_ZERO;
        }
        int[] newMag = new int[newLen];
        int limit = Math.max(0, -mov);
        for (int i = Integer.SIZE * len - 1; i >= limit; i--) {
            if ((mag[i >> 5] & (1 << (0x1F & i))) != 0) {
                int pos = i + mov;
                newMag[pos >> 5] |= 1 << (0x1F & pos);
            }
        }
        return create(newMag, sign);
    }

    public BinInt shift(int mov) {
        if (mov == 0 || isZero()) {
            return this;
        }
        if (mov % Integer.SIZE == 0) {
            return shiftI(mov / Integer.SIZE);
        }
        return shiftII(mov);
    }

    public int bitLen() {
        if (len > 0) {
            return (len << 5) - Integer.numberOfLeadingZeros(mag[len - 1]);
        } else {
            return 0;
        }
    }

    public void append(BooleanConsumer bc, boolean hsbFirst) {
        if (hsbFirst) {
            for (int i = bitLen() - 1; i >= 0; i--) {
                bc.accept((mag[i >> 5] & (1 << (0x1F & i))) != 0);
            }
        } else {
            for (int i = 0, n = bitLen(); i < n; i++) {
                bc.accept((mag[i >> 5] & (1 << (0x1F & i))) != 0);
            }
        }
    }

    public void append(BooleanConsumer bc, int len, boolean hsbFirst) {
        int bLen = bitLen();
        if (hsbFirst) {
            if (len > bLen) {
                for (int i = len - bLen; i > 0; i--) {
                    bc.accept(false);
                }
                len = bLen;
            }
            for (int i = len - 1; i >= 0; i--) {
                bc.accept((mag[i >> 5] & (1 << (0x1F & i))) != 0);
            }
        } else {
            for (int i = 0, n = Math.min(bLen, len); i < n; i++) {
                bc.accept((mag[i >> 5] & (1 << (0x1F & i))) != 0);
            }
            if (len > bLen) {
                for (int i = len - bLen; i > 0; i--) {
                    bc.accept(false);
                }
            }
        }
    }

    public boolean hasIntValue() {
        return len <= 1;
    }

    public int intValue() {
        return len == 0 ? 0 : mag[0];
    }
}
