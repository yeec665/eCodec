package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.basic.IndexBound;
import indi.hiro.common.math.sym.ui.ShownOp;

/**
 * Created by Hiro on 2018/9/21.
 */
public class TrigonometricFunction extends UnaryFunction {

    private static final IndexBound T_BOUND = new IndexBound(6);

    public static final int T_SINE = 0;
    public static final int T_COSINE = 1;
    public static final int T_TANGENT = 2;
    public static final int T_COTANGENT = 3;
    public static final int T_SECANT = 4;
    public static final int T_COSECANT = 5;

    private static final String[] SIGN = {"sin", "cos", "tan", "cot", "sec", "csc"};

    public final int type;

    public TrigonometricFunction(Expression a, int type) {
        super(a);
        T_BOUND.checkArgument(type);
        this.type = type;
    }

    @Override
    public UnaryFunction replicate(Expression a) {
        return new TrigonometricFunction(a, type);
    }

    @Override
    public int constant() {
        return C_UNCERTAIN;
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 41 + type;
        } else {
            return super.order(i);
        }
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append(SIGN[type]);
        sb.append('(');
        a.debugToString(sb);
        sb.append(')');
    }

    @Override
    public ShownOp show() {
        return null;
    }
}
