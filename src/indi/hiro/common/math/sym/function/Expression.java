package indi.hiro.common.math.sym.function;

/**
 * Created by Hiro on 2018/9/21.
 *
 * Expression
 * |-Real
 *   |-Rational
 *     |-Fraction
 *     |-FloatDecimal
 *       |-FloatDecimalWithLoop
 *     |-IntN
 *       |-BinInt
 *       |-BcdInt
 *   |-Irrational
 *     |-Pi
 *     |-Euler
 * |-Unknown
 * |-Function
 *   |-UnaryFunction
 *     |-Minus
 *     |-ExponentialFunction
 *     |-NaturalLogarithm
 *     |-TrigonometricFunction
 *   |-BinaryFunction
 *     |-Division
 *     |-Power
 *     |-Logarithm
 *   |-MvFunction
 *     |-CommutableFunction
 *       |-Sum
 *       |-Product
 */
public interface Expression extends Symbolic {

    Expression[] SAMPLE = new Expression[0];

    int C_CONST = 1;
    int C_UNCERTAIN = 0;
    int C_VAR = -1;

    int constant();

    //boolean containsUnknown(Unknown u);

    /**
     * depth = 0
     * Expression
     * |-Real
     *   |-Rational
     *     |-MiniFraction               0
     *     |-Fraction                   1
     *     |-FloatDecimal               2
     *       |-FloatDecimalWithLoop     3
     *     |-IntN
     *       |-BinInt                   4
     *       |-BcdInt                   5
     *   |-Irrational
     *     |-Pi                         11
     *     |-Euler                      12
     * |-Unknown                        21
     * |-Function
     *   |-UnaryFunction
     *     |-Minus                      31
     *     |-ExponentialFunction        32
     *     |-NaturalLogarithm           33
     *     |-TrigonometricFunction      41
     *   |-BinaryFunction
     *     |-Division                   61
     *     |-Power                      62
     *     |-Logarithm                  63
     *   |-MvFunction
     *     |-CommutableFunction
     *       |-Sum                      71
     *       |-Product                  72
     */

    int order(int i);

    int orderLength();
}
