package indi.hiro.common.math.basic;

/**
 * Created by Hiro on 2018/11/17.
 */
public class MathExtension {

    public static boolean sameSign(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0 || a < 0 && b < 0 && c < 0;
    }

    public static boolean sameSignOrZero(double a, double b, double c) {
        return a >= 0 && b >= 0 && c >= 0 || a <= 0 && b <= 0 && c <= 0;
    }
}
