package indi.hiro.common.math.sym.real;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.ui.ShownFractionLine;
import indi.hiro.common.math.sym.ui.ShownOp;

public class ConstFraction implements Real {

    final Real numerator;
    final Real denominator;

    private ConstFraction(Real numerator, Real denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public int rational() {
        if (numerator.rational() == R_RATIONAL && denominator.rational() == R_RATIONAL) {
            return R_RATIONAL;
        }
        return R_UNCERTAIN;
    }

    @Override
    public int productOrder() {
        return 0;
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
    public void debugToString(StringBuilder sb) {
        sb.append('(');
        numerator.debugToString(sb);
        sb.append(")/(");
        denominator.debugToString(sb);
        sb.append(')');
    }

    @Override
    public ShownOp show() {
        return new ShownFractionLine(numerator.show(), denominator.show());
    }

    @Override
    public int order(int i) {
        return 0;
    }

    @Override
    public int orderLength() {
        return 0;
    }
}
