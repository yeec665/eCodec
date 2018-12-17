package indi.hiro.common.math.discrete;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import indi.hiro.common.ds.util.DsToStringUtil;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Arrays;

/**
 * Created by Hiro on 2018/11/28.
 */
public class PermutationGroup {

    public static PermutationGroup createIdentity(int n) {
        return new PermutationGroup(IntegerArrays.arithmeticSequence(n, 0, 1), n);
    }

    public static PermutationGroup create(int[] seq, int n) {
        seq = Arrays.copyOf(seq, n);
        LOOP_I:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (seq[j] == i) {
                    continue LOOP_I;
                }
            }
            throw new IllegalArgumentException("Item not found : " + i);
        }
        return new PermutationGroup(seq, n);
    }

    public static void classTest() {
        PermutationGroup pg = createIdentity(12);
        System.out.println(pg);
        pg.selfShuffle(null);
        System.out.println(pg);
        String[] strings = {"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11"};
        pg.effect(strings);
        System.out.println(Arrays.asList(strings));
        System.out.println(pg);
    }

    final int[] seq;
    final int n;

    PermutationGroup(int[] seq, int n) {
        this.seq = seq;
        this.n = n;
    }

    public PermutationGroup copy() {
        return new PermutationGroup(Arrays.copyOf(seq, n), n);
    }

    public void selfInvert() {
        int[] tSeq = new int[n];
        for (int i = 0; i < n; i++) {
            tSeq[seq[i]] = i;
        }
        System.arraycopy(tSeq, 0, seq, 0, n);
    }

    public void selfLeftMultiply(PermutationGroup that) {
        for (int i = 0; i < n; i++) {
            seq[i] = that.seq[seq[i]];
        }
    }

    public void selfRightMultiply(PermutationGroup that) {
        int[] tSeq = new int[n];
        for (int i = 0; i < n; i++) {
            tSeq[i] = seq[that.seq[i]];
        }
        System.arraycopy(tSeq, 0, seq, 0, n);
    }

    /**
     * r < 0 shift left
     * r > 0 shift right
     */
    public void selfShift(int r) {

    }

    public PermutationGroup shift(int r) {
        int[] nSeq = new int[n];
        for (int i = 0; i < n; i++) {
            nSeq[FiniteField.confine(i + r, n)] = seq[i];
        }
        return new PermutationGroup(nSeq, n);
    }

    public void selfShuffle(@Nullable DiscreteRandom.ModN drn) {
        if (n < 2) {
            return;
        }
        if (drn != null) {
            drn.setN(n);
        } else {
            drn = new DiscreteRandom.ModN(n);
        }
        for (int i = 0; i < n; i++) {
            int j = drn.rndNI();
            int t = seq[i];
            seq[i] = seq[j];
            seq[j] = t;
        }
    }

    public <T> void effect(T[] array) {
        if (array.length < n) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < n; i++) {
            if ((seq[i] & Integer.MIN_VALUE) != 0) {
                continue;
            }
            T t = array[i];
            int j = i;
            while (true) {
                int k = seq[j];
                seq[j] |= Integer.MIN_VALUE;
                if (k != i) {
                    array[j] = array[k];
                } else {
                    array[j] = t;
                    break;
                }
                j = k;
            }
        }
        for (int i = 0; i < n; i++) {
            seq[i] &= Integer.MAX_VALUE;
        }
    }

    @Override
    public String toString() {
        return DsToStringUtil.toString(DsToStringUtil.nameFor(getClass()), seq, n);
    }
}
