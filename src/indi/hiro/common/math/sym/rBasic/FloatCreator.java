package indi.hiro.common.math.sym.rBasic;

import indi.hiro.common.math.sym.nBasic.BcdInt;

import java.util.ArrayList;

public class FloatCreator {

    private static byte[] plus(byte[] a, byte[] b, int len) {
        byte[] sum = new byte[len];
        int v = 0;
        for (int i = 0; i < len; i++) {
            v += a[i];
            v += b[i];
            sum[i] = (byte) (v % 10);
            v /= 10;
        }
        return sum;
    }

    private static byte[][] bcdList(BcdInt x, int lenP) {
        byte[][] list = new byte[9][];
        list[0] = new byte[lenP];
        x.copyInto(list[0], x.len);
        for (int i = 1; i < 9; i++) {
            list[i] = plus(list[i - 1], list[0], lenP);
        }
        return list;
    }

    private static void divA() {

    }

    private static FloatDecimal createDeci(byte[] a, int aLen, byte[][] bList, int lenP) {
        return null;
    }

    public static FloatDecimal create(BcdInt a, BcdInt b) {
        if (b.isZero()) {
            throw new ArithmeticException("IntN divided by zero");
        }
        if (a.isZero() || b.isOne()) {
            return FloatDecimal.create(a, 0);
        }
        int aLen = a.len;
        int lenP = b.len + 1;
        byte[] aDigits = new byte[aLen];
        a.copyInto(aDigits, aLen);
        return createDeci(aDigits, aLen, bcdList(b, lenP), lenP);
    }

    private final int sciExp;
    private final boolean sign;
    private final ArrayList<Integer> quotientList = new ArrayList<>();
    private final ArrayList<BcdInt> remainderList = new ArrayList<>();

    public FloatCreator(int sciExp, boolean sign) {
        this.sciExp = sciExp;
        this.sign = sign;
    }

    public int add(Integer i, BcdInt remainder) {
        for (int j = 0; j < remainderList.size(); j++) {
            if (remainderList.get(j).equalMe(remainder)) {
                return j;
            }
        }
        quotientList.add(i);
        remainderList.add(remainder);
        return -1;
    }

    public FloatDecimal finishWithLoop(int loopStart) {
        byte[] digits;
        digits = new byte[loopStart];
        for (int i = 0; i < loopStart; i++) {
            digits[i] = (byte) (int) quotientList.get(i);
        }
        BcdInt sContent = BcdInt.create(digits, sign);
        digits = new byte[quotientList.size() - loopStart];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (byte) (int) quotientList.get(loopStart + i);
        }
        return FloatDecimalWithLoop.create(sContent, BcdInt.create(digits, false), sciExp - loopStart + 1);
    }

    public FloatDecimal finishWithoutLoop() {
        byte[] digits = new byte[quotientList.size()];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (byte) (int) quotientList.get(i);
        }
        return FloatDecimal.create(BcdInt.create(digits, sign), sciExp - digits.length + 1);
    }
}
