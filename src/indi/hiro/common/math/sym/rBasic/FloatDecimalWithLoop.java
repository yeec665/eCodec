package indi.hiro.common.math.sym.rBasic;

import indi.hiro.common.math.sym.nBasic.BcdInt;

public class FloatDecimalWithLoop extends FloatDecimal {

    public static FloatDecimal create(BcdInt content, BcdInt loop, int exponent) {
        if (loop.isZero()) {
            return FloatDecimal.create(content, exponent);
        }
        int expInc = 0;
        while (content.readDigit(expInc) == loop.readDigit(expInc % loop.len)) {
            expInc++;
        }
        if (expInc > 0) {
            content = content.shift(-expInc);
            loop = loop.loopShift(-expInc);
            exponent += expInc;
        }
        return new FloatDecimalWithLoop(content, loop, exponent);
    }

    private final BcdInt loop;

    private FloatDecimalWithLoop(BcdInt content, BcdInt loop, int exponent) {
        super(content, exponent);
        this.loop = loop;
    }

    // rational();

    @Override
    public int productOrder() {
        return 2;
    }

    @Override
    public boolean isFiniteDec() {
        return false;
    }

    @Override
    public Fraction toReducedFraction() {
        return super.toReducedFraction();
    }

    @Override
    public Rational complement() {
        return new FloatDecimalWithLoop(content.complement(), loop, exponent);
    }

    @Override
    public Rational abs() {
        if (content.negative()) {
            return new FloatDecimalWithLoop(content.complement(), loop, exponent);
        }
        return this;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isOne() {
        return false;
    }

    @Override
    public boolean isIntegral() {
        return false;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof FloatDecimalWithLoop && equalMe((FloatDecimalWithLoop) that);
    }

    public boolean equalMe(FloatDecimalWithLoop that) {
        return content.equalMe(that.content) && loop.equalMe(that.loop) && exponent == that.exponent;
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 3;
        } else if (i == 1) {
            return exponent;
        } else {
            i -= 2;
            int lenA = content.orderLength();
            if (i < lenA) {
                return content.order(i);
            }
            return loop.order(i - lenA);
        }
    }

    @Override
    public int orderLength() {
        return 3 + content.orderLength() + loop.orderLength();
    }
}
