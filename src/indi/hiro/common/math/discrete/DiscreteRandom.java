package indi.hiro.common.math.discrete;

import indi.hiro.common.ds.primitive.IntegerArray;
import indi.hiro.common.math.basic.BoundI;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Hiro on 2018/11/28.
 */
public class DiscreteRandom {

    private static final int SS_ORI = 0xFD4F2AC1;
    private static final int SS_MUT = 0xA407A406;
    private static final int SS_ADD = 0x6CC2E658;

    private static final AtomicInteger staticSeed = new AtomicInteger(SS_ORI);

    public static int seed() {
        while (true) {
            int current = staticSeed.get();
            int next = current * SS_MUT + SS_ADD;
            if (staticSeed.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public static int seedWithTime() {
        return seed() ^ (int) System.nanoTime();
    }

    private static final int SU_MUT = 0xCD947CA9;
    private static final int SU_ADD = 0xC3;

    public static int sRndI(int x) {
        return SU_MUT * x + SU_ADD;
    }

    protected int x;

    public DiscreteRandom(int seed) {
        this.x = seed;
    }

    public DiscreteRandom() {
        this(seedWithTime());
    }

    public int rndI() {
        x = SU_MUT * x + SU_ADD;
        return x;
    }

    public int rndI(int n) {
        if (n < 2) {
            if (n < 1) {
                throw new IllegalArgumentException();
            }
            return 0;
        }
        int px;
        while (true) {
            x = SU_MUT * x + SU_ADD;
            px = Integer.MAX_VALUE & x;
            if (px + n > 0) {
                break;
            }
            if (px + (Integer.MAX_VALUE % n + 1) % n > 0) {
                break;
            }
        }
        return px % n;
    }

    public static class ModN extends DiscreteRandom {

        private int n, m;

        ModN(int n) {
            super();
            setN(n);
        }

        public void setN(int n) {
            if (n < 1) {
                throw new IllegalArgumentException();
            }
            this.n = n;
            this.m = (Integer.MAX_VALUE % n + 1) % n;
        }

        public int rndNI() {
            int px;
            do {
                x = SU_MUT * x + SU_ADD;
                px = Integer.MAX_VALUE & x;
            } while (px + m < 0);
            return px % n;
        }
    }
}
