package indi.hiro.common.math.sym.operation;

import indi.hiro.common.math.sym.function.*;
import indi.hiro.common.math.sym.nBasic.IntUtil;
import indi.hiro.common.math.sym.unknown.Unknown;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Hiro on 2018/9/21.
 */
public class Derivative implements ExpTransformation {

    private final Unknown x;

    public Derivative(Unknown x) {
        Objects.requireNonNull(x);
        this.x = x;
    }

    @Override
    public Expression effect(Expression e) {
        if (!e.containsUnknown(x)) {
            return IntUtil.DEFAULT_ZERO;
        }
        if (e instanceof Function) {
            return effectOnFunction((Function) e);
        }
        if (e == x) {
            return IntUtil.DEFAULT_ONE;
        }
        throw new OperationFailedException();
    }

    private Expression effectOnFunction(Function function) {
        if (function instanceof CommutableMvFunction) {
            if (function instanceof Sum) {
                return effectOnSum((Sum) function);
            }
            if (function instanceof Product) {
                return effectOnProduct((Product) function);
            }
        }
        if (function instanceof UnaryFunction) {
            return effectOnUnaryFunction((UnaryFunction) function);
        }
        if (function instanceof BinaryFunction) {
            return effectOnBinaryFunction((BinaryFunction) function);
        }
        throw new OperationFailedException();
    }

    private Expression effectOnSum(Sum function) {
        final int n = function.nv;
        Expression[] tx = new Expression[n];
        int c = 0;
        for (int i = 0; i < n; i++) {
            Expression var = function.var(i);
            if (var.containsUnknown(x)) {
                tx[c++] = effect(var);
            }
        }
        return Sum.create(tx, c);
    }

    private Expression effectOnProduct(Product function) {
        final int n = function.nv;
        Expression[] sx = function.copyArray();
        Product[] tx = new Product[n];
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (sx[i].containsUnknown(x)) {
                Expression[] tsx = Arrays.copyOf(sx, n);
                tsx[i] = effect(sx[i]);
                tx[c++] = new Product(tsx);
            }
        }
        return Product.create(tx, c);
    }

    private Expression effectOnUnaryFunction(UnaryFunction function) {
        Expression a = function.a;
        if (a == x) {
            return effectOnUnaryFunctionX(function);
        }
        if (function instanceof Minus) {
            return new Minus(effect(a));
        }
        if (function instanceof ExponentialFunction) {
            return new Product(function, effect(a));
        }
        if (function instanceof NaturalLogarithm) {
            return new Division(effect(a), a);
        }
        if (function instanceof TrigonometricFunction) {
            switch (((TrigonometricFunction) function).type) {
                case TrigonometricFunction.T_SINE:
                    return new Product(new TrigonometricFunction(a, TrigonometricFunction.T_COSINE), effect(a));
                case TrigonometricFunction.T_COSINE:
                    return new Minus(new Product(new TrigonometricFunction(a, TrigonometricFunction.T_SINE), effect(a)));
            }
        }
        throw new OperationFailedException();
    }

    private Expression effectOnUnaryFunctionX(UnaryFunction function) {
        if (function instanceof Minus) {
            return new Minus(x);
        }
        if (function instanceof ExponentialFunction) {
            return function;
        }
        if (function instanceof NaturalLogarithm) {
            return new Division(IntUtil.DEFAULT_ONE, x);
        }
        if (function instanceof TrigonometricFunction) {
            switch (((TrigonometricFunction) function).type) {
                case TrigonometricFunction.T_SINE:
                    return new TrigonometricFunction(x, TrigonometricFunction.T_COSINE);
                case TrigonometricFunction.T_COSINE:
                    return new Minus(new TrigonometricFunction(x, TrigonometricFunction.T_SINE));
            }
        }
        throw new OperationFailedException();
    }

    private Expression effectOnBinaryFunction(BinaryFunction function) {
        if (function instanceof Division) {
            return effectOnDivision((Division) function);
        }
        if (function instanceof Power) {
            return effectOnPower((Power) function);
        }
        throw new OperationFailedException();
    }

    private Expression effectOnDivision(Division f) {
        if (!f.a.containsUnknown(x)) {
            return new Minus(new Division(new Product(f.a, effect(f.b)),
                    new Power(f.b, IntUtil.DEFAULT_TWO)));
        }
        if (!f.b.containsUnknown(x)) {
            return new Division(effect(f.a), f.b);
        }
        return new Division(
                new Sum(new Product(effect(f.a), f.b), new Minus(new Product(f.a, effect(f.b)))),
                new Power(f.b, IntUtil.DEFAULT_TWO)
        );
    }

    private Expression effectOnPower(Power f) {
        if (!f.a.containsUnknown(x)) {
            if (f.b == x) {
                return new Product(f, new NaturalLogarithm(x));
            } else {
                return new Product(f, new NaturalLogarithm(f.b), effect(f.b));
            }
        }
        if (!f.b.containsUnknown(x)) {
            if (f.a == x) {
                return new Product(f.b, new Power(x, new Product(f.b, IntUtil.DEFAULT_M_ONE)));
            } else {
                return new Product(f.b, new Power(f.a, new Product(f.b, IntUtil.DEFAULT_M_ONE)), effect(f.a));
            }
        }
        throw new OperationFailedException();
    }
}
