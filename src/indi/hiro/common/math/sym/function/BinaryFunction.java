package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.unknown.Unknown;

import java.util.Objects;

/**
 * Created by Hiro on 2018/9/21.
 */
public abstract class BinaryFunction implements Function {

    public final Expression a, b;

    public BinaryFunction(Expression a, Expression b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        this.a = a;
        this.b = b;
    }

    public abstract BinaryFunction replicate(Expression a, Expression b);

    @Override
    public int nVar() {
        return 2;
    }

    @Override
    public Expression var(int i) {
        return (i & 1) == 0 ? a : b;
    }

    @Override
    public boolean containsUnknown(Unknown u) {
        return a.containsUnknown(u) || b.containsUnknown(u);
    }

    @Override
    public int order(int i) {
        int lenA = a.orderLength();
        if (--i < lenA) {
            return a.order(i);
        }
        return b.order(i - lenA);
    }

    @Override
    public int orderLength() {
        return 1 + a.orderLength() + b.orderLength();
    }
}
