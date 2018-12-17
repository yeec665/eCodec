package indi.hiro.common.math.sym.rBasic;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.nBasic.BcdInt;
import indi.hiro.common.math.sym.nBasic.IntN;
import indi.hiro.common.math.sym.nBasic.IntOp;
import indi.hiro.common.math.sym.ui.ShownFractionLine;
import indi.hiro.common.math.sym.ui.ShownOp;

public class Fraction implements Rational {

    private static Fraction createBcd(BcdInt a, BcdInt b) {
        return null;
    }

    private static Fraction createBcdA(BcdInt a, int n) {
        return null;
    }

    private static Fraction createBcd9(BcdInt a, int n) {
        return null;
    }

    public static boolean equal(Fraction a, Fraction b) {
        return false;
    }

    private final IntN numerator;
    private final IntN denominator;

    private boolean fullyReduced;

    public Fraction(IntN numerator, IntN denominator) {
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
        //TODO
        return false;
    }

    @Override
    public FloatDecimal toFloatDecimal() {
        //TODO
        return null;
    }

    @Override
    public double dValue() {
        return numerator.dValue() / denominator.dValue();
    }

    @Override
    public DoubleWithE dwe() {
        return new DoubleWithE(dValue());
    }

    @Override
    public IntN ceil() {
        return null;
    }

    @Override
    public IntN floor() {
        return null;
    }

    @Override
    public Rational complement() {
        if (numerator.isZero()) {
            return this;
        }
        return new Fraction(numerator.complement(), denominator);
    }

    @Override
    public Rational abs() {
        if (numerator.negative()) {
            return new Fraction(numerator.complement(), denominator);
        }
        return this;
    }

    @Override
    public boolean positive() {
        return numerator.positive();
    }

    @Override
    public boolean negative() {
        return numerator.negative();
    }

    @Override
    public boolean isZero() {
        return numerator.isZero();
    }

    @Override
    public boolean isOne() {
        return IntOp.equal(numerator, denominator);
    }

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || (that instanceof Fraction && equalMe((Fraction) that));
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equalMe(Fraction that) {
        if (this.fullyReduced && that.fullyReduced) {
            return IntOp.equal(this.numerator, that.numerator) && IntOp.equal(this.denominator, that.denominator);
        } else if (this == that) {
            return true;
        } else {
            return IntOp.equal(IntOp.multiply(this.numerator, that.denominator, 0), IntOp.multiply(this.denominator, that.numerator, 0));
        }
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 1;
        } else {
            i--;
            int lenA = numerator.orderLength();
            if (i < lenA) {
                return numerator.order(i);
            }
            return denominator.order(i - lenA);
        }
    }

    @Override
    public int orderLength() {
        return 1 + numerator.orderLength() + denominator.orderLength();
    }

    @Override
    public void debugToString(StringBuilder sb) {
        numerator.debugToString(sb);
        sb.append('/');
        denominator.debugToString(sb);
    }

    @Override
    public ShownOp show() {
        return new ShownFractionLine(numerator.show(), denominator.show());
    }

    @Override
    public String toString() {
        return "Fraction[" + numerator + " / " + denominator + "]";
    }
}
