package indi.hiro.common.math.basic;

/**
 * Created by Hiro on 2018/11/29.
 */
public class Factor {

    public static int gcd(int a, int b) {
        if (!(a > 0 && b > 0)) {
            throw new IllegalArgumentException();
        }
        if (a < b) {
            return innerGcd(b, a);
        } else {
            return innerGcd(a, b);
        }
    }

    private static int innerGcd(int a, int b) {
        int r;
        do {
            r = a % b;
            a = b;
            b = r;
        } while (r != 0);
        return a;
    }
}
