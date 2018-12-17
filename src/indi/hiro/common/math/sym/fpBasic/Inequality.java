package indi.hiro.common.math.sym.fpBasic;

public class Inequality {

    public static int sign(double x) {
        return x > 0 ? 1 : (x < 0 ? -1 : 0);
    }

    public static double min(double a, double b) {
        return a > b ? b : a;
    }

    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    public static double min(double a, double b, double c) {
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    public static double max(double a, double b, double c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static double min(double a, double b, double c, double d) {
        return min(min(a, b), min(c, d));
    }

    public static double max(double a, double b, double c, double d) {
        return max(max(a, b), max(c, d));
    }

    public static double minAbs(double... xx) {
        double minAbs = Double.POSITIVE_INFINITY;
        for (double x : xx) {
            if (x < 0) {
                x = -x;
            }
            if (x < minAbs) {
                minAbs = x;
            }
        }
        return minAbs;
    }

    public static double maxAbs(double... xx) {
        double maxAbs = 0;
        for (double x : xx) {
            if (x < 0) {
                x = -x;
            }
            if (x > maxAbs) {
                maxAbs = x;
            }
        }
        return maxAbs;
    }

    public static double absMin(double... xx) {
        double minAbs = Double.POSITIVE_INFINITY, minX = Double.POSITIVE_INFINITY;
        for (double x : xx) {
            double abs;
            if (x < 0) {
                abs = -x;
            } else {
                abs = x;
            }
            if (abs < minAbs) {
                minAbs = abs;
                minX = x;
            }
        }
        return minX;
    }

    public static double absMax(double... xx) {
        double maxAbs = 0, maxX = 0;
        for (double x : xx) {
            double abs;
            if (x < 0) {
                abs = -x;
            } else {
                abs = x;
            }
            if (abs > maxAbs) {
                maxAbs = abs;
                maxX = x;
            }
        }
        return maxX;
    }
}
