package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.nBasic.BcdInt;
import indi.hiro.common.math.sym.ui.ShownOp;

/**
 * Created by Hiro on 2018/9/21.
 */
public class Product extends CommutableMvFunction {

    public static Expression create(Expression[] x, int len) {
        if (len > 1) {
            return new Product(x, len);
        } else if (len == 1) {
            return x[0];
        } else {
            return BcdInt.BCD_ONE;
        }
    }

    public Product(Expression[] x) {
        super(x);
    }

    public Product(Expression[] x, int len) {
        super(x, len);
    }

    public Product(Expression a, Expression b) {
        super(new Expression[]{a, b});
    }

    public Product(Expression a, Expression b, Expression c) {
        super(new Expression[]{a, b, c});
    }

    @Override
    public MvFunction replicate(Expression[] x) {
        return new Product(x);
    }

    @Override
    public int constant() {
        return C_UNCERTAIN;
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 72;
        } else {
            return super.order(i);
        }
    }

    @Override
    public void debugToString(StringBuilder sb) {
        for (int i = 0; i < nv; i++) {
            if (i > 0) {
                sb.append(" * ");
            }
            sb.append('(');
            x[i].debugToString(sb);
            sb.append(')');
        }
    }

    @Override
    public ShownOp show() {
        return null;
    }
}
