package indi.hiro.common.math.sym.nBasic;

public class FinitePrimeLink {

    public static final FinitePrimeLink P_2 = new FinitePrimeLink(0, 2);
    public static final FinitePrimeLink P_3 = new FinitePrimeLink(1, 3);
    public static final FinitePrimeLink P_5 = new FinitePrimeLink(2, 5);
    static {
        P_2.next = P_3;
        P_3.next = P_5;
    }

    public static boolean isPrime(int x) {
        if (x < 2) {
            return false;
        }
        int limit = 1 << (16 - Integer.numberOfLeadingZeros(x) / 2);
        FinitePrimeLink n = P_2;
        while (n.v < limit) {
            if (x % n.v == 0) {
                return false;
            }
            n = n.getNext();
        }
        return true;
    }

    public static FinitePrimeLink smallestFactor(int x) {
        if (x < 2) {
            return null;
        }
        int limit = 1 << (16 - Integer.numberOfLeadingZeros(x) / 2);
        FinitePrimeLink n = P_2;
        while (n.v < limit) {
            if (x % n.v == 0) {
                return n;
            }
            n = n.getNext();
        }
        while (n.v < x) {
            n = n.getNext();
        }
        return n;
    }

    public final int i;
    public final int v;
    private FinitePrimeLink next;

    private FinitePrimeLink(int i, int v) {
        this.i = i;
        this.v = v;
    }

    public FinitePrimeLink getNext() {
        if (next == null) {
            for (int x = v + 1; x < Integer.MAX_VALUE; x++) {
                if (isPrime(x)) {
                    next = new FinitePrimeLink(i + 1, x);
                    break;
                }
            }
        }
        return next;
    }

    @Override
    public String toString() {
        return String.format("fPrime[%d, %d]", v, i);
    }
}
