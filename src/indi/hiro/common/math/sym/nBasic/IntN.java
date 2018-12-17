package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.rBasic.FloatDecimal;
import indi.hiro.common.math.sym.rBasic.Rational;
import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownString;

public abstract class IntN implements Rational {

    public final int len;

    public final boolean sign;

    public IntN(int len, boolean sign) {
        this.len = len;
        this.sign = sign;
    }

    // rational();

    @Override
    public int productOrder() {
        return 0;
    }

    @Override
    public boolean isFiniteDec() {
        return true;
    }

    @Override
    public FloatDecimal toFloatDecimal() {
        return FloatDecimal.create(toBcdInt(), 0);
    }

    public abstract BinInt toBinInt();

    public abstract BcdInt toBcdInt();

    // dValue();

    @Override
    public DoubleWithE dwe() {
        return new DoubleWithE(dValue());
    }

    @Override
    public IntN ceil() {
        return this;
    }

    @Override
    public IntN floor() {
        return this;
    }

    public abstract IntN complement();

    public IntN abs() {
        if (negative()) {
            return complement();
        }
        return this;
    }

    public abstract IntN myZero();

    public abstract IntN myOne();

    @Override
    public boolean positive() {
        return !sign && len > 0;
    }

    @Override
    public boolean negative() {
        return sign;
    }

    public boolean isZero() {
        return len == 0;
    }

    // boolean isOne();


    @Override
    public boolean isIntegral() {
        return true;
    }

    public abstract String represent(int radix);

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append(represent(10));
    }

    @Override
    public ShownOp show() {
        return new ShownString(represent(10));
    }
}
