package indi.hiro.common.math.sym.function;

/**
 * Created by Hiro on 2018/9/21.
 *
 * Function
 * |-UnaryFunction
 *   |-Minus
 *   |-ExponentialFunction
 *   |-NaturalLogarithm
 *   |-TrigonometricFunction
 * |-BinaryFunction
 *   |-Division
 *   |-Power
 *   |-Logarithm
 * |-MvFunction
 *   |-CommutableFunction
 *     |-Sum
 *     |-Product
 */
public interface Function extends Expression {

    int nVar();

    Expression var(int i);

    //boolean containsUnknown(Unknown u);
}
