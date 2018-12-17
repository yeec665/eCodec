package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.ui.ShownOp;

/**
 * Created by Hiro on 2018/9/21.
 */
public class Minus extends UnaryFunction {

    public Minus(Expression a) {
        super(a);
    }

    @Override
    public UnaryFunction replicate(Expression a) {
        return new Minus(a);
    }

    @Override
    public int constant() {
        return a.constant();
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 31;
        } else {
            return super.order(i);
        }
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append("-(");
        a.debugToString(sb);
        sb.append(')');
    }

    @Override
    public ShownOp show() {
        return null;
    }
}
