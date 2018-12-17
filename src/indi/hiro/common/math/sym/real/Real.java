package indi.hiro.common.math.sym.real;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.function.Expression;
import indi.hiro.common.math.sym.unknown.Unknown;

public interface Real extends Expression {

    @Override
    default boolean containsUnknown(Unknown u) {
        return false;
    }

    @Override
    default int constant() {
        return Expression.C_CONST;
    }

    int R_RATIONAL = 1;
    int R_UNCERTAIN = 0;
    int R_IRRATIONAL = -1;

    int rational();

    int productOrder();

    double dValue();

    DoubleWithE dwe();

    // debugToString();

    // show();
}
