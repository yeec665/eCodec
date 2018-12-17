package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;

import java.util.Random;

public class IntOpTester {

    private final Random random = new Random();

    public IntOpTester() {

    }

    private int randomInt() {
        switch (random.nextInt(12)) {
            case 0:
                return -2;
            case 1:
                return -1;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return Integer.MAX_VALUE - 1;
            case 5:
                return Integer.MAX_VALUE;
            case 6:
                return Integer.MIN_VALUE;
            case 7:
                return Integer.MIN_VALUE + 1;
            default:
                return random.nextInt();
        }
    }

    private int[] randomIntArray() {
        int[] out = new int[1 + random.nextInt(3)];
        for (int i = 0; i < out.length; i++) {
            out[i] = randomInt();
        }
        return out;
    }

    private BinInt randomBinInt() {
        return BinInt.create(randomIntArray(), random.nextBoolean());
    }

    private int randomDigit() {
        switch (random.nextInt(5)) {
            case 0:
                return 0;
            case 1:
                return 9;
            default:
                return random.nextInt(10);
        }
    }

    private byte[] randomBcdArray() {
        byte[] out = new byte[random.nextInt(13)];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) randomDigit();
        }
        return out;
    }

    private BcdInt randomBcdInt() {
        return BcdInt.create(randomBcdArray(), random.nextBoolean());
    }

    public boolean test4(IntN a, IntN b) {
        int op = random.nextInt(4);
        if (op == 0) {
            IntN c = IntOp.plus(a, b, 0);
            if (c.dwe().overlap(DoubleWithE.sum(a.dwe(), b.dwe()))) {
                return true;
            } else {
                System.out.println("Plus test failed : " + a + " + " + b + " = " + c);
                return false;
            }
        } else if (op == 1) {
            IntN c = IntOp.minus(a, b, 0);
            if (c.dwe().overlap(DoubleWithE.difference(a.dwe(), b.dwe()))) {
                return true;
            } else {
                System.out.println("Minus test failed : " + a + " - " + b + " = " + c);
                return false;
            }
        } else if (op == 2) {
            IntN c = IntOp.multiply(a, b, 0);
            if (c.dwe().overlap(DoubleWithE.product(a.dwe(), b.dwe()))) {
                return true;
            } else {
                System.out.println("Multiply test failed : " + a + " * " + b + " = " + c);
                return false;
            }
        } else if (op == 3) {
            try {
                IntDivResult c = IntOp.divide(a, b, 0);
                if (IntOp.equal(a, IntOp.plus(IntOp.multiply(b, c.quotient, 0), c.remainder, 0))) {
                    return true;
                } else {
                    System.out.println("Division test failed : " + a + " / " + b + " = " + c);
                    return false;
                }
            } catch (ArithmeticException ignored) {

            }
        }
        return true;
    }

    public boolean binIntTest() {
        BinInt a = randomBinInt();
        BinInt b = randomBinInt();
        return test4(a, b);
    }

    public void binIntTest(int n) {
        System.out.println("Bin int test begins");
        int passed = 0;
        for (int i = 0; i < n; i++) {
            if (binIntTest()) {
                passed++;
            }
        }
        System.out.printf("Bin int test : %d/%d\n", passed, n);
    }

    public boolean bcdIntTest() {
        IntN a = randomBcdInt();
        IntN b = randomBcdInt();
        return test4(a, b);
    }

    public void bcdIntTest(int n) {
        System.out.println("Bcd int test begins");
        int passed = 0;
        for (int i = 0; i < n; i++) {
            if (bcdIntTest()) {
                passed++;
            }
        }
        System.out.printf("Bcd int test : %d/%d\n", passed, n);
    }

    public boolean mixedIntTest() {
        IntN a = random.nextBoolean() ? randomBinInt() : randomBcdInt();
        IntN b = random.nextBoolean() ? randomBinInt() : randomBcdInt();
        return test4(a, b);
    }

    public void mixedIntTest(int n) {
        System.out.println("Mixed int test begins");
        int passed = 0;
        for (int i = 0; i < n; i++) {
            if (mixedIntTest()) {
                passed++;
            }
        }
        System.out.printf("Mixed int test : %d/%d\n", passed, n);
    }

    public void syntheticTest() {
        binIntTest(500);
        bcdIntTest(500);
        mixedIntTest(500);
    }

    private void exTest() {
        BinInt a = BinInt.create(new int[]{0x4295176B, 0x00000003}, false);
        BinInt b = BinInt.create(new int[]{0x00000002}, false);
        System.out.println(a);
        System.out.println(b);
        System.out.println(IntOp.divide(a, b, 0));
    }

    private void eyTest() {
        BcdInt a = randomBcdInt();
        BcdInt b = randomBcdInt();
        IntDivResult c;
        if (!b.isZero()) {
            c = IntOp.divide(a.toBinInt(), b.toBinInt(), 0);
            if (IntOp.equal(a, IntOp.plus(IntOp.multiply(b, c.quotient, 0), c.remainder, 0))) {
                return;
            }
            System.out.println("Division test : " + a.toBinInt() + " / " + b.toBinInt() + " = " + c);
            c = IntOp.divide(a, b, 0);
            System.out.println("Division verify : " + c.quotient.toBinInt() + "..." + c.remainder.toBinInt());
        }
    }
}
