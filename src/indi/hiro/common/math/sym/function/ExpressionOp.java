package indi.hiro.common.math.sym.function;

/**
 * Created by Hiro on 2018/9/22.
 */
public class ExpressionOp {

    public static int compareOrder(Expression a, Expression b) {
        int d = a.order(0) - b.order(0);
        if (d != 0) {
            return d;
        }
        int lenA = a.orderLength();
        int lenB = b.orderLength();
        int lim = Math.min(lenA, lenB);
        for (int i = 1; i < lim; i++) {
            d = a.order(i) - b.order(i);
            if (d != 0) {
                return d;
            }
        }
        return lenA - lenB;
    }

    public static boolean identicallyEqual(Expression a, Expression b) {
        return a == b || compareOrder(a, b) == 0;
    }
}
