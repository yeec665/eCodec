package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.ui.ShownOp;

/**
 * Created by Hiro on 2018/9/22.
 */
public class Power extends BinaryFunction {

    public Power(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    public BinaryFunction replicate(Expression a, Expression b) {
        return new Power(a, b);
    }

    @Override
    public int constant() {
        return C_UNCERTAIN;
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 62;
        } else {
            return super.order(i);
        }
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append('(');
        a.debugToString(sb);
        sb.append(")/(");
        b.debugToString(sb);
        sb.append(')');
    }

    @Override
    public ShownOp show() {
        return null;
    }
}
