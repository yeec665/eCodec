package indi.hiro.common.math.discrete;

import indi.hiro.common.math.basic.Primes;

/**
 * Created by Hiro on 2018/11/19.
 *
 * In mathematics, a finite field or Galois field is a field that contains a finite number of elements.
 */
public class FiniteField {

    public static final FiniteField FF_3 = new FiniteField(3);

    public static FiniteField forMod(int n) {
        if (n < 2) {
            throw new IllegalArgumentException();
        }
        if ((n & (n - 1)) == 0) {
            return new FiniteField2N(n);
        } else {
            return new FiniteField(n);
        }
    }

    public static FiniteField forPrimeNotSmaller(int n) {
        if (n <= 2) {
            return FiniteField2N.FF_2;
        } else {
            return new FiniteField(Primes.nextPrime(n));
        }
    }

    public static FiniteField forPrimeNotLarger(int n) {
        if (n <= 2) {
            return FiniteField2N.FF_2;
        } else {
            return new FiniteField(Primes.prevPrime(n));
        }
    }

    public static int confine(int x, int n) {
        if (x < 0) {
            x = x % n;
            if (x != 0) {
                x += n;
            }
        } else if (x >= n) {
            x %= n;
        }
        return x;
    }

    final int mod;

    FiniteField(int mod) {
        if (mod < 2) {
            throw new IllegalArgumentException();
        }
        this.mod = mod;
    }

    public int confine(int x) {
        if (x < 0) {
            x = x % mod;
            if (x != 0) {
                x += mod;
            }
        } else if (x >= mod) {
            x %= mod;
        }
        return x;
    }

    static class FiniteField2N extends FiniteField {

        static final FiniteField FF_2 = new FiniteField2N(2);

        final int mask;

        FiniteField2N(int n) {
            super(n);
            mask = n - 1;
        }

        @Override
        public int confine(int x) {
            return mask & x;
        }
    }
}
