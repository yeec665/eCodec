package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.unknown.Unknown;

import java.util.Objects;

/**
 * Created by Hiro on 2018/9/21.
 */
public abstract class UnaryFunction implements Function {

    public final Expression a;

    public UnaryFunction(Expression a) {
        Objects.requireNonNull(a);
        this.a = a;
    }

    public abstract UnaryFunction replicate(Expression a);

    @Override
    public int nVar() {
        return 1;
    }

    @Override
    public Expression var(int i) {
        return a;
    }

    @Override
    public boolean containsUnknown(Unknown u) {
        return a.containsUnknown(u);
    }

    @Override
    public int order(int i) {
        return a.order(i - 1);
    }

    @Override
    public int orderLength() {
        return 1 + a.orderLength();
    }
}
