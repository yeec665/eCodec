package indi.hiro.common.math.sym.unknown;

import indi.hiro.common.math.sym.function.*;
import indi.hiro.common.math.sym.nBasic.IntUtil;
import indi.hiro.common.math.sym.operation.ExpTransformation;
import indi.hiro.common.math.sym.operation.OperationFailedException;
import indi.hiro.common.math.sym.rBasic.Rational;
import indi.hiro.common.math.sym.rBasic.RationalOp;
import indi.hiro.common.math.sym.real.Euler;
import indi.hiro.common.math.sym.real.Pi;
import indi.hiro.common.math.sym.real.Real;
import indi.hiro.common.math.sym.real.RealOp;

/**
 * Created by Hiro on 2018/9/22.
 */
public class RealUnite implements ExpTransformation {

    private final int config;

    public RealUnite(int config) {
        this.config = config;
    }

    @Override
    public Expression effect(Expression e) {
        if (e instanceof MvFunction) {
            return effectOnMvFunction((MvFunction) e);
        }
        if (e instanceof BinaryFunction) {
            return effectOnBinaryFunction((BinaryFunction) e);
        }
        if (e instanceof UnaryFunction) {
            return effectOnUnaryFunction((UnaryFunction) e);
        }
        return e;
    }

    private Expression effectOnMvFunction(MvFunction function) {
        if (function instanceof Sum) {
            return effectOnSum((Sum) function);
        }
        if (function instanceof Product) {
            return effectOnProduct((Product) function);
        }
        throw new OperationFailedException();
    }

    private Expression effectOnSum(Sum function) {
        final int n = function.nv;
        Expression[] tx = new Expression[n];
        Rational rSum = null;
        int c = 0;
        boolean changed = false;
        for (int i = 0; i < n; i++) {
            Expression v1 = function.var(i);
            Expression v2 = effect(v1);
            if (v2 instanceof Rational) {
                if (rSum == null) {
                    rSum = (Rational) v2;
                } else {
                    rSum = RationalOp.plus(rSum, (Rational) v2, config);
                }
            } else {
                tx[c++] = v2;
            }
            changed |= v1 != v2;
        }
        if (!changed) {
            return function;
        }
        if (rSum != null && !rSum.isZero()) {
            tx[c++] = rSum;
        }
        return Sum.create(tx, c);
    }

    private Expression effectOnProduct(Product function) {
        final int n = function.nv;
        Expression[] tx = new Expression[n];
        Rational rProduct = null;
        int c = 0;
        boolean changed = false;
        for (int i = 0; i < n; i++) {
            Expression v1 = function.var(i);
            Expression v2 = effect(v1);
            if (v2 instanceof Rational) {
                if (rProduct == null) {
                    rProduct = (Rational) v2;
                } else {
                    rProduct = RationalOp.multiply(rProduct, (Rational) v2, config);
                }
            } else {
                tx[c++] = v2;
            }
            changed |= v1 != v2;
        }
        if (!changed) {
            return function;
        }
        if (rProduct != null) {
            if (rProduct.isZero()) {
                return IntUtil.DEFAULT_ZERO;
            }
            if (!rProduct.isOne()) {
                System.arraycopy(tx, 0, tx, 1, c++);
                tx[0] = rProduct;
            }
        }
        return Product.create(tx, c);
    }

    private Expression effectOnBinaryFunction(BinaryFunction function) {
        if (function instanceof Division) {
            return effectOnDivision((Division) function);
        }
        if (function instanceof Power) {

        }
        return function;
    }

    private Expression effectOnDivision(Division f) {
        Expression a = f.a;
        Expression b = f.b;
        Expression ta = effect(a);
        Expression tb = effect(b);
        if (tb instanceof Rational) {
            Rational rtb = (Rational) tb;
            if (rtb.isOne()) {
                return ta;
            }
            if (rtb.isZero()) {
                throw new ArithmeticException();
            }
            if (ta instanceof Rational) {
                return RationalOp.divide((Rational) ta, rtb, config);
            }
        }
        if (a == ta && b == tb) {
            return f;
        } else {
            return new Division(ta, tb);
        }
    }

    private Expression effectOnUnaryFunction(UnaryFunction function) {
        Expression x = function.a;
        Expression tx = effect(x);
        if (tx instanceof Real) {
            if (function instanceof Minus && tx instanceof Rational) {
                return ((Rational) tx).complement();
            }
            if (function instanceof ExponentialFunction && tx instanceof Rational) {
                Rational rtx = (Rational) tx;
                if (rtx.isZero()) {
                    return IntUtil.DEFAULT_ONE;
                }
                if (rtx.isOne()) {
                    return new Euler();
                }
            }
            if (function instanceof NaturalLogarithm) {
                if (RealOp.compareToZero((Real) tx) <= 0) {
                    throw new ArithmeticException();
                }
                if (RealOp.equal((Real) tx, IntUtil.DEFAULT_ONE)) {
                    return IntUtil.DEFAULT_ZERO;
                }
                if (tx instanceof Euler) {
                    return IntUtil.DEFAULT_ONE;
                }
            }
            if (function instanceof TrigonometricFunction) {
                return effectOnTrigonometricFunction((TrigonometricFunction) function, (Real) tx);
            }
        }
        if (x == tx) {
            return function;
        } else {
            return function.replicate(tx);
        }
    }

    private Expression effectOnTrigonometricFunction(TrigonometricFunction function, Real tx) {
        if (RealOp.equal(tx, IntUtil.DEFAULT_ZERO)) {
            switch (function.type) {
                case TrigonometricFunction.T_SINE:
                case TrigonometricFunction.T_TANGENT:
                    return IntUtil.DEFAULT_ZERO;
                case TrigonometricFunction.T_COSINE:
                case TrigonometricFunction.T_SECANT:
                    return IntUtil.DEFAULT_ONE;
                case TrigonometricFunction.T_COTANGENT:
                case TrigonometricFunction.T_COSECANT:
                    throw new ArithmeticException();
            }
        }
        if (tx instanceof Pi) {
            switch (function.type) {
                case TrigonometricFunction.T_SINE:
                case TrigonometricFunction.T_TANGENT:
                    return IntUtil.DEFAULT_ZERO;
                case TrigonometricFunction.T_COSINE:
                case TrigonometricFunction.T_SECANT:
                    return IntUtil.DEFAULT_M_ONE;
                case TrigonometricFunction.T_COTANGENT:
                case TrigonometricFunction.T_COSECANT:
                    throw new ArithmeticException();
            }
        }
        return function;
    }
}
