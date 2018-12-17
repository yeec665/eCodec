package indi.hiro.common.math.sym.real;

import com.sun.istack.internal.Nullable;
import indi.hiro.common.math.sym.rBasic.Rational;
import indi.hiro.common.math.sym.rBasic.RationalOp;

public class RealOp {

    public static boolean equal(Real a, Real b) {
        if (a instanceof Rational && b instanceof Rational) {
            return RationalOp.equal((Rational) a, (Rational) b);
        }
        return a.dwe().overlap(b.dwe()) && deepEqual(a, b);
    }

    private static boolean deepEqual(Real a, Real b) {
        return false;
    }

    public static int compare(Real a, Real b) {
        return 0;
    }

    public static int compareToZero(Real a) {
        if (a instanceof Rational) {
            Rational ra = (Rational) a;
            if (ra.isZero()) {
                return 0;
            } else if (ra.negative()) {
                return -1;
            } else {
                return 1;
            }
        }
        if (a instanceof Pi || a instanceof Euler) {
            return 1;
        }
        throw new ArithmeticException();
    }

    @Nullable
    public static Real plus(Real a, Real b, int c) {
        return a;
    }
}
