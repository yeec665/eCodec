package indi.hiro.common.math.sym.rBasic;

import com.sun.istack.internal.Nullable;
import indi.hiro.common.math.sym.nBasic.IntN;
import indi.hiro.common.math.sym.nBasic.IntOp;
import indi.hiro.common.math.sym.real.RealOpConfig;

public class RationalOp implements RealOpConfig {

    public static boolean equal(Rational a, Rational b) {
        if (a instanceof IntN) {
            return b instanceof IntN && IntOp.equal((IntN) a, (IntN) b);
        } else {
            return !(b instanceof IntN) && deepEqual(a, b);
        }
    }

    private static boolean deepEqual(Rational a, Rational b) {
        if (a instanceof Fraction) {
            if (b instanceof Fraction) {
                return Fraction.equal((Fraction) a, (Fraction) b);
            } else if (b instanceof FloatDecimal) {
                return Fraction.equal((Fraction) a, ((FloatDecimal) b).toReducedFraction());
            }
        } else if (a instanceof FloatDecimal) {
            if (b instanceof FloatDecimal) {
                return ((FloatDecimal) a).equalMe((FloatDecimal) b);
            } else if (b instanceof Fraction) {
                return Fraction.equal(((FloatDecimal) a).toReducedFraction(), (Fraction) b);
            }
        }
        return false;
    }

    public static Rational plus(Rational a, Rational b, int c) {
        return a;
    }

    public static Rational multiply(Rational a, Rational b, int c) {
        return a;
    }

    public static Rational divide(Rational a, Rational b, int c) {
        return a;
    }

    @Nullable
    public static Rational power(Rational a, Rational b, int c) {
        return a;
    }
}
