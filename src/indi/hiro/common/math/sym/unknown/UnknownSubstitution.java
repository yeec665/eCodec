package indi.hiro.common.math.sym.unknown;

import indi.hiro.common.math.sym.function.*;
import indi.hiro.common.math.sym.operation.ExpTransformation;

import java.util.Objects;

/**
 * Created by Hiro on 2018/9/21.
 */
public class UnknownSubstitution implements ExpTransformation {

    private final Unknown a;
    private final Expression b;

    public UnknownSubstitution(Unknown a, Expression b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        this.a = a;
        this.b = b;
    }

    @Override
    public Expression effect(Expression e) {
        if (e instanceof Function) {
            return effectOnFunction((Function) e);
        }
        if (e == a) {
            return b;
        }
        return e;
    }

    private Expression effectOnFunction(Function function) {
        if (function instanceof UnaryFunction) {
            UnaryFunction unaryFunction = (UnaryFunction) function;
            Expression ta = effect(unaryFunction.a);
            return ta == unaryFunction.a ? function : unaryFunction.replicate(ta);
        }
        if (function instanceof BinaryFunction) {
            BinaryFunction binaryFunction = (BinaryFunction) function;
            Expression ta = effect(binaryFunction.a);
            Expression tb = effect(binaryFunction.b);
            return ta == binaryFunction.a && tb == binaryFunction.b ? function :
                    binaryFunction.replicate(ta, tb);
        }
        if (function instanceof MvFunction) {
            MvFunction mvFunction = (MvFunction) function;
            Expression[] tx = new Expression[mvFunction.nv];
            boolean changed = false;
            for (int i = 0; i < tx.length; i++) {
                Expression x = mvFunction.var(i);
                tx[i] = effect(x);
                changed |= tx[i] != x;
            }
            return changed ? mvFunction.replicate(tx) : function;
        }
        return function;
    }
}
