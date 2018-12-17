package indi.hiro.common.math.basic;

/**
 * Created by Hiro on 2018/12/2.
 *
 * z = a + bi
 */
public class ComplexNumber {

    public static void plus(ComplexNumber z1, ComplexNumber z2, ComplexNumber rv) {
        rv.a = z1.a + z2.a;
        rv.b = z1.b + z2.b;
    }

    public static void minus(ComplexNumber z1, ComplexNumber z2, ComplexNumber rv) {
        rv.a = z1.a - z2.a;
        rv.b = z1.b - z2.b;
    }

    public static void multiply(ComplexNumber z1, ComplexNumber z2, ComplexNumber rv) {
        double ra = z1.a * z2.a - z1.b * z2.b;
        double rb = z1.a * z2.b + z1.b * z2.a;
        rv.a = ra;
        rv.b = rb;
    }

    public static void divide(ComplexNumber z1, ComplexNumber z2, ComplexNumber rv) {
        double ra = z1.a * z2.a + z1.b * z2.b;
        double rb = z1.b * z2.a - z1.a * z2.b;
        double mm = z2.sqModule();
        rv.a = ra / mm;
        rv.b = rb / mm;
    }

    public static void exp(ComplexNumber z, ComplexNumber rv) {
        double m = Math.exp(z.a);
        rv.a = m * Math.cos(z.b);
        rv.b = m * Math.sin(z.b);
    }

    public double a, b;

    public ComplexNumber() {

    }

    public ComplexNumber(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double sqModule() {
        return a * a + b * b;
    }

    public double module() {
        return Math.sqrt(a * a + b * b);
    }

    public double argument() {
        return Math.atan2(b, a);
    }
}
