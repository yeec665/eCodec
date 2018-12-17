package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.rBasic.Fraction;
import indi.hiro.common.math.sym.real.RealOpConfig;

public class IntOp implements RealOpConfig {

    public static boolean equal(IntN a, IntN b) {
        if (a.sign != b.sign) {
            return false;
        }
        if (a instanceof BcdInt && b instanceof BcdInt) {
            return ((BcdInt) a).equalMe((BcdInt) b);
        }
        return a.toBinInt().equalMe(b.toBinInt());
    }

    public static int compare(IntN a, IntN b) {
        if (a.sign != b.sign) {
            return a.sign ? -1 : 1;
        }
        if (a instanceof BcdInt && b instanceof BcdInt) {
            return BcdInt.compare((BcdInt) a, (BcdInt) b);
        }
        return BinInt.compare(a.toBinInt(), b.toBinInt());
    }

    public static IntN plus(IntN a, IntN b, int c) {
        IntN z = null;
        if (a.isZero()) {
            z = b;
        } else if (b.isZero()) {
            z = a;
        }
        if (z != null) {
            if ((c & FORCE_USE_INT) == 0) {
                return z;
            } else if ((c & PREFER_BCD_INT) == 0) {
                return z.toBcdInt();
            } else {
                return z.toBinInt();
            }
        }
        if ((c & FORCE_USE_INT) == 0) {
            if (a instanceof BcdInt && b instanceof BcdInt) {
                return BcdInt.plus((BcdInt) a, (BcdInt) b);
            }
            if (a instanceof BinInt && b instanceof BinInt) {
                return BinInt.plus((BinInt) a, (BinInt) b);
            }
        }
        if ((c & PREFER_BCD_INT) == 0) {
            return BinInt.plus(a.toBinInt(), b.toBinInt());
        } else {
            return BcdInt.plus(a.toBcdInt(), b.toBcdInt());
        }
    }

    public static IntN minus(IntN a, IntN b, int c) {
        IntN z = null;
        if (a.isZero()) {
            z = b.complement();
        } else if (b.isZero()) {
            z = a;
        }
        if (z != null) {
            if ((c & FORCE_USE_INT) == 0) {
                return z;
            } else if ((c & PREFER_BCD_INT) == 0) {
                return z.toBcdInt();
            } else {
                return z.toBinInt();
            }
        }
        if ((c & FORCE_USE_INT) == 0) {
            if (a instanceof BcdInt && b instanceof BcdInt) {
                return BcdInt.minus((BcdInt) a, (BcdInt) b);
            }
            if (a instanceof BinInt && b instanceof BinInt) {
                return BinInt.minus((BinInt) a, (BinInt) b);
            }
        }
        if ((c & PREFER_BCD_INT) == 0) {
            return BinInt.minus(a.toBinInt(), b.toBinInt());
        } else {
            return BcdInt.minus(a.toBcdInt(), b.toBcdInt());
        }
    }

    public static IntN multiply(IntN a, IntN b, int c) {
        IntN z = null;
        if (a.isZero() || b.isOne()) {
            z = a;
        }
        if (b.isZero() || a.isOne()) {
            z = b;
        }
        if (z != null) {
            if ((c & FORCE_USE_INT) == 0) {
                return z;
            } else if ((c & PREFER_BCD_INT) == 0) {
                return z.toBcdInt();
            } else {
                return z.toBinInt();
            }
        }
        if ((c & FORCE_USE_INT) == 0) {
            if (a instanceof BcdInt && b instanceof BcdInt) {
                return BcdInt.multiply((BcdInt) a, (BcdInt) b);
            }
            if (a instanceof BinInt && b instanceof BinInt) {
                return BinInt.multiply((BinInt) a, (BinInt) b);
            }
        }
        if ((c & PREFER_BCD_INT) == 0) {
            return BinInt.multiply(a.toBinInt(), b.toBinInt());
        } else {
            return BcdInt.multiply(a.toBcdInt(), b.toBcdInt());
        }
    }

    public static IntDivResult<? extends IntN> divide(IntN a, IntN b, int c) {
        if (b.isZero()) {
            throw new ArithmeticException("IntN divided by zero");
        }
        if (a.isZero() || b.isOne()) {
            if ((c & FORCE_USE_INT) == 0) {
                return new IntDivResult<>(a, a.myZero());
            } else if ((c & PREFER_BCD_INT) == 0) {
                return new IntDivResult<>(a.toBcdInt(), BcdInt.BCD_ZERO);
            } else {
                return new IntDivResult<>(b.toBinInt(), BinInt.BIN_ZERO);
            }
        }
        if ((c & FORCE_USE_INT) == 0) {
            if (a instanceof BinInt && b instanceof BinInt) {
                return BinInt.divide((BinInt) a, (BinInt) b);
            }
            if (a instanceof BcdInt && b instanceof BcdInt) {
                return BcdInt.divide((BcdInt) a, (BcdInt) b);
            }
        }
        if ((c & PREFER_BCD_INT) == 0) {
            return BinInt.divide(a.toBinInt(), b.toBinInt());
        } else {
            return BcdInt.divide(a.toBcdInt(), b.toBcdInt());
        }
    }

    public static BinInt powerBinI(BinInt a, int b) {
        if (b < 2) {
            if (b == 1) {
                return a;
            } else {
                return BinInt.BIN_ONE;
            }
        }
        BinInt c = a;
        for (int i = 30 - Integer.numberOfLeadingZeros(b); i >= 0; i--) {
            c = BinInt.multiply(c, c);
            if ((b & (1 << i)) != 0) {
                c = BinInt.multiply(c, a);
            }
        }
        return c;
    }

    private static BinInt gcdBinWithZZXCF(BinInt a, BinInt b) {
        if (a.sign) {
            a = a.complement();
        }
        if (b.sign) {
            b = b.complement();
        }
        int c = BinInt.compare(a, b);
        if (c <= 0) {
            if (c == 0) {
                return a;
            }
            BinInt t = a;
            a = b;
            b = t;
        }
        while (!b.isZero()) {
            BinInt t = BinInt.remainder(a, b);
            a = b;
            b = t;
        }
        return a;
    }

    private static BinInt gcdBinWithGXJSS(BinInt a, BinInt b) {
        if (a.sign) {
            a = a.complement();
        }
        if (b.sign) {
            b = b.complement();
        }
        int s = Math.min(a.numberOfTrailingZeros(), b.numberOfTrailingZeros());
        if (s > 0) {
            a = a.shift(-s);
            b = b.shift(-s);
        }
        int c = BinInt.compare(a, b);
        if (c <= 0) {
            if (c == 0) {
                return a;
            }
            BinInt t = a;
            a = b;
            b = t;
        }
        while (!b.isZero()) {
            BinInt t = BinInt.minus(a, b);
            a = b;
            b = t;
        }
        return a.shift(s);
    }

    private static final BinInt ZZXCF_THRESHOLD_BIN = BinInt.create(1000);

    private static Fraction reduceBin(BinInt a, BinInt b, int c) {
        BinInt gcd;
        if ((c & (FORCE_ZZXCF | FORCE_GXJSS)) == 0) {
            if (compare(a.abs(), ZZXCF_THRESHOLD_BIN) == 1) {
                c |= FORCE_ZZXCF;
            }
        }
        if ((c & FORCE_ZZXCF) != 0) {
            gcd = gcdBinWithZZXCF(a, b);
        } else {
            gcd = gcdBinWithGXJSS(a, b);
        }
        return new Fraction(BinInt.quotient(a, gcd), BinInt.quotient(b, gcd));
    }

    private static BcdInt gcdBcdWithZZXCF(BcdInt a, BcdInt b) {
        if (a.sign) {
            a = a.complement();
        }
        if (b.sign) {
            b = b.complement();
        }
        int c = BcdInt.compare(a, b);
        if (c <= 0) {
            if (c == 0) {
                return a;
            }
            BcdInt t = a;
            a = b;
            b = t;
        }
        while (!b.isZero()) {
            BcdInt t = BcdInt.remainder(a, b);
            a = b;
            b = t;
        }
        return a;
    }

    private static BcdInt gcdBcdWithGXJSS(BcdInt a, BcdInt b) {
        // TODO
        return BcdInt.BCD_ONE;
    }

    private static final BinInt ZZXCF_THRESHOLD_BCD = BinInt.create(200);

    private static Fraction reduceBcd(BcdInt a, BcdInt b, int c) {
        BcdInt gcd;
        if ((c & (FORCE_ZZXCF | FORCE_GXJSS)) == 0) {
            if (compare(a.abs(), ZZXCF_THRESHOLD_BCD) == 1) {
                c |= FORCE_ZZXCF;
            }
        }
        if ((c & FORCE_ZZXCF) != 0) {
            gcd = gcdBcdWithZZXCF(a, b);
        } else {
            gcd = gcdBcdWithGXJSS(a, b);
        }
        return new Fraction(BcdInt.quotient(a, gcd), BcdInt.quotient(b, gcd));
    }
    
    public static Fraction reduce(IntN a, IntN b, int c) {
        if ((c & FORCE_USE_INT) == 0) {
            if (a instanceof BinInt && b instanceof BinInt) {
                return reduceBin((BinInt) a, (BinInt) b, c);
            }
            if (a instanceof BcdInt && b instanceof BcdInt) {
                return reduceBcd((BcdInt) a, (BcdInt) b, c);
            }
        }
        if ((c & PREFER_BCD_INT) == 0) {
            return reduceBin(a.toBinInt(), b.toBinInt(), c);
        } else {
            return reduceBcd(a.toBcdInt(), b.toBcdInt(), c);
        }
    }

    public static IntN sqrtFloor(IntN x, int c) {
        return null;
    }

    public static IntN sqrtCeil(IntN x, int c) {
        return null;
    }
}
