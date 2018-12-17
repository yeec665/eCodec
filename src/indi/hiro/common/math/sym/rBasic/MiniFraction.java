package indi.hiro.common.math.sym.rBasic;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.nBasic.BinInt;
import indi.hiro.common.math.sym.nBasic.IntN;
import indi.hiro.common.math.sym.ui.ShownFractionLine;
import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownString;
import indi.hiro.common.math.sym.util.SymCorruptedException;

/**
 * Created by Hiro on 2018/9/26.
 */
public class MiniFraction implements Rational {

    public static int gcd(int a, int b) {
        if (a <= 0) {
            if (a == 0) {
                return 1;
            }
            a = -a;
        }
        if (b <= 0) {
            if (b == 0) {
                return 1;
            }
            b = -b;
        }
        if (a >= b) {
            return innerGcd(a, b);
        } else {
            return innerGcd(b, a);
        }
    }

    private static int innerGcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    @Nullable
    public static MiniFraction create(int a, int b) {
        if (Math.abs((long) a) + Math.abs((long) b) > Integer.MAX_VALUE) {
            return null;
        }
        if (b <= 0) {
            if (b == 0) {
                return null;
            }
            a = -a;
            b = -b;
        }
        return innerCreate(a, b);
    }

    @NotNull
    private static MiniFraction innerCreate(int a, int b) {
        if (a == 0) {
            return MF_ZERO;
        }
        int gcd = innerGcd(Math.abs(a), b);
        if (gcd > 1) {
            a /= gcd;
            b /= gcd;
        }
        return new MiniFraction(a, b);
    }

    private static Fraction longCreate(long a, long b) {
        return null;
    }

    public static Rational plus(MiniFraction a, MiniFraction b) {
        long numerator = (long) a.numerator * b.denominator + (long) a.denominator * b.numerator;
        long denominator = (long) a.denominator * b.denominator;
        if (Math.abs(numerator) + Math.abs(denominator) > Integer.MAX_VALUE) {
            return longCreate(numerator, denominator);
        }
        return innerCreate((int) numerator, (int) denominator);
    }

    public static Rational minus(MiniFraction a, MiniFraction b) {
        long numerator = (long) a.numerator * b.denominator - (long) a.denominator * b.numerator;
        long denominator = (long) a.denominator * b.denominator;
        if (Math.abs(numerator) + Math.abs(denominator) > Integer.MAX_VALUE) {
            return longCreate(numerator, denominator);
        }
        return innerCreate((int) numerator, (int) denominator);
    }

    public static Rational mulitply(MiniFraction a, MiniFraction b) {
        long numerator = (long) a.numerator * b.numerator;
        long denominator = (long) a.denominator * b.denominator;
        if (Math.abs(numerator) + Math.abs(denominator) > Integer.MAX_VALUE) {
            return longCreate(numerator, denominator);
        }
        return innerCreate((int) numerator, (int) denominator);
    }

    public static Rational divide(MiniFraction a, MiniFraction b) {
        long numerator = (long) a.numerator * b.denominator;
        long denominator = (long) a.denominator * b.numerator;
        if (denominator == 0) {
            throw new ArithmeticException();
        }
        if (Math.abs(numerator) + Math.abs(denominator) > Integer.MAX_VALUE) {
            return longCreate(numerator, denominator);
        }
        return innerCreate((int) numerator, (int) denominator);
    }

    public static final MiniFraction MF_ZERO = new MiniFraction(0, 1);

    public final int numerator;
    public final int denominator;

    private MiniFraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    // rational();


    @Override
    public int productOrder() {
        return 3;
    }

    @Override
    public boolean isFiniteDec() {
        int x = denominator;
        if (x <= 0) {
            throw new SymCorruptedException();
        }
        while (x % 5 == 0) {
            x /= 5;
        }
        while (x % 2 == 0) {
            x /= 2;
        }
        return x == 1;
    }

    @Override
    public FloatDecimal toFloatDecimal() {
        return null;
    }

    @Override
    public double dValue() {
        return (double) numerator / denominator;
    }

    @Override
    public DoubleWithE dwe() {
        return new DoubleWithE(dValue());
    }

    @Override
    public IntN ceil() {
        if (numerator > 0) {
            return BinInt.create((numerator + denominator - 1) / denominator);
        } else {
            return BinInt.create(numerator / denominator);
        }
    }

    @Override
    public IntN floor() {
        if (numerator > 0) {
            return BinInt.create(numerator / denominator);
        } else {
            return BinInt.create((numerator + denominator - 1) / denominator);
        }
    }

    @Override
    public Rational complement() {
        return new MiniFraction(-numerator, denominator);
    }

    @Override
    public Rational abs() {
        if (numerator < 0) {
            return new MiniFraction(numerator, denominator);
        }
        return this;
    }

    @Override
    public boolean positive() {
        return numerator > 0;
    }

    @Override
    public boolean negative() {
        return numerator < 0;
    }

    @Override
    public boolean isZero() {
        return numerator == 0;
    }

    @Override
    public boolean isOne() {
        return numerator == denominator;
    }

    @Override
    public boolean isIntegral() {
        return denominator == 1;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || (that instanceof MiniFraction && equalMe((MiniFraction) that));
    }

    public boolean equalMe(MiniFraction that) {
        return this.numerator == that.numerator && this.denominator == that.denominator;
    }

    @Override
    public int order(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return numerator;
            case 2:
                return denominator;
            default:
                return -1;
        }
    }

    @Override
    public int orderLength() {
        return 3;
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append(numerator);
        sb.append('/');
        sb.append(denominator);
    }

    @Override
    public ShownOp show() {
        return new ShownFractionLine(
                new ShownString(String.valueOf(numerator)),
                new ShownString(String.valueOf(denominator))
        );
    }

    @Override
    public String toString() {
        return "MiniFraction[" + numerator + " / " + denominator + "]";
    }
}
