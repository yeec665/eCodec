package indi.hiro.common.math.sym.rBasic;

import indi.hiro.common.math.sym.nBasic.IntN;
import indi.hiro.common.math.sym.real.Real;

public interface Rational extends Real {

    @Override
    default int rational() {
        return R_RATIONAL;
    }

    //int productOrder();

    boolean isFiniteDec();

    FloatDecimal toFloatDecimal();

    // dValue();

    // dwe();

    IntN ceil();

    IntN floor();

    Rational complement();

    Rational abs();

    boolean positive();

    boolean negative();

    boolean isZero();

    boolean isOne();

    boolean isIntegral();

    // debugToString();

    // show();
}
