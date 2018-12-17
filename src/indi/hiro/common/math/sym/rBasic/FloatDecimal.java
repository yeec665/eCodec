package indi.hiro.common.math.sym.rBasic;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.nBasic.BcdInt;
import indi.hiro.common.math.sym.nBasic.IntN;
import indi.hiro.common.math.sym.nBasic.IntUtil;
import indi.hiro.common.math.sym.ui.*;

public class FloatDecimal implements Rational {

    public static final FloatDecimal FD_ZERO = new FloatDecimal(BcdInt.BCD_ZERO, 0);
    private static final int POOL_MIN = -16;
    private static final int POOL_MAX = 15;
    private static final int POOL_MASK = 0x1F;
    private static final FloatDecimal[] POOL = new FloatDecimal[POOL_MAX - POOL_MIN + 1];
    static {
        for (int i = POOL_MIN; i <= POOL_MAX; i++) {
            POOL[POOL_MASK & i] = new FloatDecimal(BcdInt.BCD_ONE, i);
        }
    }
    public static final FloatDecimal FD_ONE = POOL[0];
    public static final FloatDecimal FD_TEN = POOL[1];

    public static FloatDecimal create(BcdInt content, int exponent) {
        if (content.isZero()) {
            return FD_ZERO;
        }
        int expInc = 0;
        while (content.readDigit(expInc) == 0) expInc++;
        if (expInc > 0) {
            content = content.shift(-expInc);
            exponent += expInc;
        }
        if (content.isOne() && IntUtil.inClosedInterval(POOL_MIN, exponent, POOL_MAX)) {
            return POOL[POOL_MASK & exponent];
        }
        return new FloatDecimal(content, exponent);
    }

    public static FloatDecimal plus(FloatDecimal a, FloatDecimal b) {
        return a;
    }

    public static FloatDecimal minus(FloatDecimal a, FloatDecimal b) {
        return a;
    }

    public static FloatDecimal multiply(FloatDecimal a, FloatDecimal b) {
        return a;
    }

    public static FloatDecimal divide(FloatDecimal a, FloatDecimal b) {
        return a;
    }

    final BcdInt content;
    final int exponent;

    FloatDecimal(BcdInt content, int exponent) {
        this.content = content;
        this.exponent = exponent;
    }

    // rational();

    @Override
    public int productOrder() {
        return 1;
    }

    @Override
    public boolean isFiniteDec() {
        return true;
    }

    public Fraction toReducedFraction() {
        //TODO
        return null;
    }

    @Override
    public FloatDecimal toFloatDecimal() {
        return this;
    }

    @Override
    public double dValue() {
        return content.dValue() * Math.pow(10.0, exponent);
    }

    @Override
    public DoubleWithE dwe() {
        DoubleWithE dwe;
        if (exponent > 0) {
            dwe = new DoubleWithE(DoubleWithE.power(10.0, exponent));
        } else if (exponent < 0) {
            dwe = new DoubleWithE(DoubleWithE.power(0.1, -exponent));
        } else {
            return content.dwe();
        }
        return DoubleWithE.product(content.dwe(), dwe);
    }

    @Override
    public IntN ceil() {
        if (exponent < 0 && content.negative()) {
            return BcdInt.minus(content.shift(exponent), BcdInt.BCD_ONE);
        }
        return content.shift(exponent);
    }

    @Override
    public IntN floor() {
        if (exponent < 0 && content.positive()) {
            return BcdInt.plus(content.shift(exponent), BcdInt.BCD_ONE);
        }
        return content.shift(exponent);
    }

    @Override
    public Rational complement() {
        if (this == FD_ZERO) {
            return FD_ZERO;
        }
        return new FloatDecimal(content.complement(), exponent);
    }

    @Override
    public Rational abs() {
        if (content.negative()) {
            return new FloatDecimal(content.complement(), exponent);
        }
        return this;
    }

    @Override
    public boolean positive() {
        return content.positive();
    }

    @Override
    public boolean negative() {
        return content.negative();
    }

    @Override
    public boolean isZero() {
        return this == FD_ZERO;
    }

    @Override
    public boolean isOne() {
        return this == FD_ONE;
    }

    @Override
    public boolean isIntegral() {
        return exponent >= 0;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof FloatDecimal && equalMe((FloatDecimal) that);
    }

    public boolean equalMe(FloatDecimal that) {
        return content.equalMe(that.content) && exponent == that.exponent;
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 2;
        } else if (i == 1) {
            return exponent;
        } else {
            return content.order(i - 2);
        }
    }

    @Override
    public int orderLength() {
        return 2 + content.orderLength();
    }

    private void appendSci(StringBuilder sb) {
        if (content.negative()) {
            sb.append('-');
        }
        int start = content.len - 1;
        for (int i = start; i >= 0; i--) {
            sb.append((char) ('0' + content.readDigit(i)));
            if (i == start) {
                sb.append('.');
            }
        }
        sb.append('E');
        sb.append(start + exponent);
    }

    @Override
    public void debugToString(StringBuilder sb) {
        if (exponent >= 0) {
            if (exponent > 3) {
                appendSci(sb);
            } else {
                content.appendTo(sb);
                for (int i = 0; i < exponent; i++) {
                    sb.append('0');
                }
            }
        } else {
            if (content.len + exponent > -3) {
                if (content.negative()) {
                    sb.append('-');
                }
                for (int i = Math.max(1 - exponent, content.len) - 1; i >= 0; i--) {
                    sb.append((char) ('0' + content.readDigit(i)));
                    if (i + exponent == 0) {
                        sb.append('.');
                    }
                }
            } else {
                appendSci(sb);
            }
        }
    }

    private ShownOp sciRepresent() {
        StringBuilder sb = new StringBuilder();
        if (content.negative()) {
            sb.append('-');
        }
        int start = content.len - 1;
        for (int i = start; i >= 0; i--) {
            sb.append((char) ('0' + content.readDigit(i)));
            if (i == start) {
                sb.append('.');
            }
        }
        ShownArray shownArray = new ShownArray(new ShownString(sb.toString()), new ShownSymbol(ShownSymbol.SYMBOL_MULTIPLY_4));
        shownArray.addLast(new ShownRearScript(new ShownString("10"), new ShownString(Integer.toString(start + exponent))));
        return shownArray;
    }

    @Override
    public ShownOp show() {
        if (exponent == 0) {
            return content.show();
        }
        if (exponent < 0 && content.len + exponent > -3) {
            StringBuilder sb = new StringBuilder();
            if (content.negative()) {
                sb.append('-');
            }
            for (int i = Math.max(1 - exponent, content.len) - 1; i >= 0; i--) {
                sb.append((char) ('0' + content.readDigit(i)));
                if (i + exponent == 0) {
                    sb.append('.');
                }
            }
            return new ShownString(sb.toString());
        }
        if (exponent <= 3) {
            StringBuilder sb = new StringBuilder();
            content.appendTo(sb);
            for (int i = 0; i < exponent; i++) {
                sb.append('0');
            }
            return new ShownString(sb.toString());
        }
        return sciRepresent();
    }

    @Override
    public String toString() {
        return "FloatDecimal[" + content + "E" + exponent + "]";
    }
}
