package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.util.SymCorruptedException;

public class BcdParser {

    public static final BcdInt INT_MIN = parse(Integer.MIN_VALUE);
    public static final BcdInt INT_MAX = parse(Integer.MAX_VALUE);

    public static BcdInt parse(int x) {
        long lx = x;
        boolean sign = lx < 0;
        if (sign) {
            lx = -lx;
        }
        byte[] digits = new byte[IntUtil.ceilDivide(64 - Long.numberOfLeadingZeros(lx), 3)];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (byte) (lx % 10);
            lx /= 10;
        }
        if (lx != 0) {
            throw new SymCorruptedException();
        }
        return BcdInt.create(digits, sign);
    }

    public static boolean decParseCheck(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && (i != 0 || (c != '-' && c != '+'))) {
                return false;
            }
        }
        return true;
    }

    public static BcdInt parseBcd(String s) {
        boolean sign = false;
        int offset = 0;
        if (s.charAt(0) == '-') {
            sign = true;
            offset = 1;
        } else if (s.charAt(0) == '+') {
            offset = 1;
        }
        byte[] digits = new byte[s.length() - offset];
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i] = (byte) (s.charAt(offset++) - '0');
        }
        return BcdInt.create(digits, sign);
    }

    public static String represent(BcdInt x, int radix) {
        IntUtil.checkRadix(radix);
        boolean sign = false;
        if (x.negative()) {
            sign = true;
            x = x.complement();
        }
        BcdInt r = parse(radix);
        StringBuilder sb = new StringBuilder();
        while (!x.isZero()) {
            IntDivResult divResult = BcdInt.divide(x, r);
            x = (BcdInt) divResult.quotient;
            sb.append(((BcdInt) divResult.remainder).digitRepresent());
        }
        if (sign) {
            sb.append('-');
        }
        return sb.reverse().toString();
    }
}
