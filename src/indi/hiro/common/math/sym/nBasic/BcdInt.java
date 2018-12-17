package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownString;
import indi.hiro.common.math.sym.util.SymCorruptedException;

public class BcdInt extends IntN {

    private static final int POOL_MAX = 9;
    private static final BcdInt[] POOL = new BcdInt[POOL_MAX + 1];
    static {
        POOL[0] = new BcdInt(new byte[0], false);
        for (int i = POOL_MAX; i > 0; i--) {
            POOL[i] = new BcdInt(new byte[]{(byte) i}, false);
        }
    }
    public static final BcdInt BCD_ZERO = POOL[0];
    public static final BcdInt BCD_ONE = POOL[1];
    public static final BcdInt BCD_TWO = POOL[2];
    public static final BcdInt BCD_M_ONE = create(BCD_ONE.digits, true);

    public static BcdInt create(byte[] digits, boolean sign) {
        int len = digits.length;
        while (len > 0 && digits[len - 1] == 0) {
            len--;
        }
        for (int i = len - 1; i >= 0; i--) {
            if (!IntUtil.validDecDigit(digits[i])) {
                throw new SymCorruptedException();
            }
        }
        if (len == 0) {
            return BCD_ZERO;
        } else if (len == 1 && !sign) {
            return POOL[digits[0]];
        } else {
            return new BcdInt(digits, len, sign);
        }
    }

    public static int compare(byte[] a, int aLen, byte[] b, int bLen) {
        if (aLen != bLen) {
            return Integer.compare(aLen, bLen);
        } else {
            for (int i = aLen - 1; i >= 0; i--) {
                if (a[i] != b[i]) {
                    return a[i] > b[i] ? 1 : -1;
                }
            }
            return 0;
        }
    }

    public static int compare(BcdInt a, BcdInt b) {
        if (a.sign != b.sign) {
            return a.sign ? -1 : 1;
        } else {
            int v = compare(a.digits, a.len, b.digits, b.len);
            return a.sign ? -v : v;
        }
    }

    private static byte[] plus(byte[] a, int aLen, byte[] b, int bLen) {
        if (aLen < bLen) {
            byte[] t = a;
            a = b;
            b = t;
            int tLen = aLen;
            aLen = bLen;
            bLen = tLen;
        }
        byte[] sum;
        if (aLen == bLen && a[aLen - 1] + b[aLen - 1] >= 9 || a[aLen - 1] >= 9) {
            sum = new byte[aLen + 1];
        } else {
            sum = new byte[aLen];
        }
        int v = 0;
        for (int i = 0; i < aLen; i++) {
            v += a[i];
            if (i < bLen) {
                v += b[i];
            }
            sum[i] = (byte) (v % 10);
            v /= 10;
        }
        if (aLen < sum.length) {
            sum[aLen] = (byte) v;
        }
        return sum;
    }

    private static byte[] minus(byte[] a, int aLen, byte[] b, int bLen) {
        byte[] sum = new byte[aLen];
        int v = 0;
        for (int i = 0; i < aLen; i++) {
            v += a[i];
            if (i < bLen) {
                v -= b[i];
            }
            if (v < 0) {
                sum[i] = (byte) (v + 10);
                v = -1;
            } else {
                sum[i] = (byte) v;
                v = 0;
            }
        }
        return sum;
    }

    public static BcdInt plus(BcdInt a, BcdInt b) {
        if (a.sign == b.sign) {
            return create(plus(a.digits, a.len, b.digits, b.len), a.sign);
        } else {
            int v = compare(a.digits, a.len, b.digits, b.len);
            if (v == 0) {
                return BCD_ZERO;
            } else if (v > 0) {
                return create(minus(a.digits, a.len, b.digits, b.len), a.sign);
            } else {
                return create(minus(b.digits, b.len, a.digits, a.len), b.sign);
            }
        }
    }

    public static BcdInt minus(BcdInt a, BcdInt b) {
        if (a.sign != b.sign) {
            return create(plus(a.digits, a.len, b.digits, b.len), a.sign);
        } else {
            int v = compare(a.digits, a.len, b.digits, b.len);
            if (v == 0) {
                return BCD_ZERO;
            } else if (v > 0) {
                return create(minus(a.digits, a.len, b.digits, b.len), a.sign);
            } else {
                return create(minus(b.digits, b.len, a.digits, a.len), !b.sign);
            }
        }
    }

    private static byte[] shortMultiply(byte[] a, int aLen, byte[] b, int bLen) {
        byte[] product = new byte[aLen + bLen];
        for (int i = 0; i < bLen; i++) {
            int bi = b[i];
            if (bi == 0) {
                continue;
            }
            int v = 0;
            for (int j = 0; j < aLen; j++) {
                v += a[j] * bi;
                product[i + j] += v % 10;
                v /= 10;
            }
            product[i + aLen] += v;
        }
        int v = 0;
        for (int i = 0; i < product.length; i++) {
            v += product[i];
            product[i] = (byte) (v % 10);
            v /= 10;
        }
        return product;
    }

    private static void plusTo(byte[] x, int d, int mov) {
        while (d > 0) {
            d += x[mov];
            x[mov] = (byte) (d % 10);
            d /= 10;
            mov++;
        }
    }

    private static byte[] longMultiply(byte[] a, int aLen, byte[] b, int bLen) {
        byte[] product = new byte[aLen + bLen];
        for (int i = 0; i < bLen; i++) {
            for (int j = 0; j < aLen; j++) {
                plusTo(product, a[j] * b[i], i + j);
            }
        }
        return product;
    }

    public static byte[] multiply(byte[] a, int aLen, byte[] b, int bLen) {
        if (aLen < bLen) {
            byte[] t = a;
            a = b;
            b = t;
            int tLen = aLen;
            aLen = bLen;
            bLen = tLen;
        }
        if (bLen < 10) {
            return shortMultiply(a, aLen, b, bLen);
        } else {
            return longMultiply(a, aLen, b, bLen);
        }
    }

    public static BcdInt multiply(BcdInt a, BcdInt b) {
        return create(multiply(a.digits, a.len, b.digits, b.len), a.sign ^ b.sign);
    }

    private static int compare(byte[] a, byte[] b, int bLen, int mov) {
        if (a[bLen + mov] > 0) {
            return 1;
        }
        for (int i = bLen - 1; i >= 0; i--) {
            int x = a[i + mov] - b[i];
            if (x != 0) {
                return x;
            }
        }
        return 0;
    }

    private static int divide(byte[] a, byte[] b, int bLen, int mov) {
        for (int x = 0; x < 9; x++) {
            if (compare(a, b, bLen, mov) < 0) {
                return x;
            }
            int v = 0;
            for (int i = 0; i < bLen; i++) {
                v += a[i + mov] - b[i];
                if (v < 0) {
                    a[i + mov] = (byte) (v + 10);
                    v = -1;
                } else {
                    a[i + mov] = (byte) v;
                    v = 0;
                }
            }
            a[bLen + mov] += v;
        }
        return 9;
    }

    private static IntDivResult<BcdInt> innerDivide(BcdInt a, BcdInt b) {
        byte[] remainder = new byte[a.len + 1];
        System.arraycopy(a.digits, 0, remainder, 0, a.len);
        byte[] quotient = new byte[a.len - b.len + 1];
        for (int mov = quotient.length - 1; mov >= 0; mov--) {
            quotient[mov] = (byte) divide(remainder, b.digits, b.len, mov);
        }
        return new IntDivResult<>(create(quotient, a.sign ^ b.sign), create(remainder, a.sign));
    }

    private static BcdInt innerQuotient(BcdInt a, BcdInt b) {
        byte[] remainder = new byte[a.len + 1];
        System.arraycopy(a.digits, 0, remainder, 0, a.len);
        byte[] quotient = new byte[a.len - b.len + 1];
        for (int mov = quotient.length - 1; mov >= 0; mov--) {
            quotient[mov] = (byte) divide(remainder, b.digits, b.len, mov);
        }
        return create(quotient, a.sign ^ b.sign);
    }

    private static BcdInt innerRemainder(BcdInt a, BcdInt b) {
        byte[] remainder = new byte[a.len + 1];
        System.arraycopy(a.digits, 0, remainder, 0, a.len);
        for (int mov = a.len - b.len; mov >= 0; mov--) {
            divide(remainder, b.digits, b.len, mov);
        }
        return create(remainder, a.sign);
    }

    public static IntDivResult<BcdInt> divide(BcdInt a, BcdInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BcdInt divided by zero");
        }
        int v = compare(a.digits, a.len, b.digits, b.len);
        if (v < 0) {
            return new IntDivResult<>(BCD_ZERO, a);
        } else if (v == 0) {
            return new IntDivResult<>((a.sign == b.sign) ? BCD_ONE : BCD_M_ONE, BCD_ZERO);
        } else {
            return innerDivide(a, b);
        }
    }

    public static BcdInt quotient(BcdInt a, BcdInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BcdInt divided by zero");
        }
        int v = compare(a.digits, a.len, b.digits, b.len);
        if (v < 0) {
            return BCD_ZERO;
        } else if (v == 0) {
            return (a.sign == b.sign) ? BCD_ONE : BCD_M_ONE;
        } else {
            return innerQuotient(a, b);
        }
    }

    public static BcdInt remainder(BcdInt a, BcdInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("BcdInt divided by zero");
        }
        int v = compare(a.digits, a.len, b.digits, b.len);
        if (v < 0) {
            return a;
        } else if (v == 0) {
            return BCD_ZERO;
        } else {
            return innerRemainder(a, b);
        }
    }

    public static BcdInt divideDigit(BcdInt a, int b) {
        if (b < 2 || b > 9) {
            if (b == 1) {
                return a;
            }
            throw new IllegalArgumentException();
        }
        byte[] quotient = new byte[a.len];
        int c = 0;
        for (int i = quotient.length - 1; i >= 0; i--) {
            c *= 10;
            c += a.digits[i];
            quotient[i] = (byte) (c / b);
            c %= b;
        }
        return create(quotient, a.sign);
    }

    private final byte[] digits; // from LSB to MSB

    BcdInt(byte[] digits, int len, boolean sign) {
        super(len, sign);
        this.digits = digits;
    }

    BcdInt(byte[] digits, boolean sign) {
        super(digits.length, sign);
        this.digits = digits;
    }

    // rational();

    // finite();


    @Override
    public BinInt toBinInt() {
        if (hasIntValue()) {
            return BinInt.create(intValue());
        }
        BinInt bi = BinInt.create(digits[len - 1]);
        for (int i = len - 2; i >= 0; i--) {
            bi = BinInt.multiply(bi, BinInt.create(10));
            int v = digits[i];
            if (v > 0) {
                bi = BinInt.plus(bi, BinInt.create(v));
            }
        }
        return sign ? bi.complement() : bi;
    }

    @Override
    public BcdInt toBcdInt() {
        return this;
    }

    @Override
    public double dValue() {
        double v = 0;
        for (int i = len - 1; i >= 0; i--) {
            v *= 10;
            v += digits[i];
        }
        return sign ? -v : v;
    }

    // dwe();

    // ceil();

    // floor();

    @Override
    public BcdInt complement() {
        if (len == 0) {
            return this;
        }
        return new BcdInt(digits, len, !sign);
    }

    // abs();

    @Override
    public IntN myZero() {
        return BCD_ZERO;
    }

    @Override
    public IntN myOne() {
        return BCD_ONE;
    }

    // positive();

    // negative();

    // isZero();

    @Override
    public boolean isOne() {
        return this == BCD_ONE;
    }

    public void appendTo(StringBuilder sb) {
        if (sign) {
            sb.append("-");
        }
        for (int i = len - 1; i >= 0; i--) {
            sb.append((char) ('0' + digits[i]));
        }
    }

    @Override
    public String represent(int radix) {
        if (radix == 10) {
            StringBuilder sb = new StringBuilder();
            appendTo(sb);
            return sb.toString();
        }
        IntUtil.checkRadix(radix);
        if (hasIntValue()) {
            return Integer.toString(intValue(), radix);
        }
        return BcdParser.represent(this, radix);
    }

    @Override
    public ShownOp show() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return new ShownString(sb.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BcdInt[");
        appendTo(sb);
        return sb.append(']').toString();
    }

    public boolean equalMe(BcdInt that) {
        if (this.sign ^ that.sign) {
            return false;
        }
        if (this.len != that.len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (this.digits[i] != that.digits[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof BcdInt && equalMe((BcdInt) that);
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
                return digits[i];
            } else {
                return -1;
            }
        }
    }

    @Override
    public int orderLength() {
        return 2 + len;
    }

    public void copyInto(byte[] array, int aLen) {
        System.arraycopy(digits, 0, array, 0, aLen);
    }

    public int readDigit(int index) {
        if (index >= 0 && index < len) {
            return digits[index];
        }
        return 0;
    }

    public int digitSum() {
        int sum = 0;
        for (int i = len - 1; i >= 0; i--) {
            int x = digits[i];
            if (!IntUtil.validDecDigit(x)) {
                throw new SymCorruptedException();
            }
            sum += x;
        }
        return sum;
    }

    public char digitRepresent() {
        int len = digits.length;
        if (len == 0) {
            return '0';
        } else if (len == 1) {
            return (char) ('0' + digits[0]);
        } else if (len == 2) {
            int v = digits[0] + 10 * digits[1] - 10;
            if (v >= 0 && v < 26) {
                return (char) (v + 'A');
            }
        }
        return ' ';
    }

    public BcdInt shift(int mov) {
        if (mov == 0 || isZero()) {
            return this;
        }
        int newLen = len + mov;
        if (newLen <= 0) {
            return BCD_ZERO;
        }
        byte[] newDigits = new byte[newLen];
        if (mov > 0) {
            // shift left
            System.arraycopy(digits, 0, newDigits, mov, len);
        } else {
            // shift right
            System.arraycopy(digits, -mov, newDigits, 0, newLen);
        }
        return create(newDigits, sign);
    }

    public BcdInt loopShift(int mov) {
        if (isZero()) {
            return this;
        }
        mov = IntUtil.positiveMod(mov, len);
        if (mov == 0) {
            return this;
        }
        byte[] newDigits = new byte[len];
        System.arraycopy(digits, 0, newDigits, mov, len - mov);
        System.arraycopy(digits, len - mov, newDigits, 0, mov);
        return create(newDigits, sign);
    }

    public boolean hasIntValue() {
        return sign && compare(BcdParser.INT_MIN, this) <= 0 || !sign && compare(this, BcdParser.INT_MAX) <= 0;
    }

    public int intValue() {
        long v = 0;
        for (int i = digits.length - 1; i >= 0; i--) {
            v *= 10;
            v += digits[i];
        }
        return sign ? (int) -v : (int) v;
    }
}
