package indi.hiro.common.math.sym.fpBasic;

public class FpUtil {

    public static double dotProduct(double x1, double y1, double x2, double y2) {
        return x1 * y1 + x2 * y2;
    }

    public static double cosA(double x1, double y1, double x2, double y2) {
        return (x1 * y1 + x2 * y2) / Math.sqrt((x1 * x1 + y1 * y1) * (x2 * x2 + y2 * y2));
    }
}
