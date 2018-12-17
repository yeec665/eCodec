package indi.hiro.common.ds.primitive;

import indi.hiro.common.ds.generic.ArraySequence;
import indi.hiro.common.ds.util.DsToStringUtil;
import indi.hiro.common.math.basic.BoundI;

/**
 * Created by Hiro on 2018/11/23.
 */
public class BooleanSymmetricMatrix {

    public static BooleanSymmetricMatrix createFalse(int n) {
        BoundI.checkPositive(n);
        return new BooleanSymmetricMatrix(n, BooleanBuilder.createFalseWithCapacity(n * (n - 1) / 2));
    }

    public static void classTest() {
        BooleanSymmetricMatrix bsm = BooleanSymmetricMatrix.createFalse(20);
        bsm.set(true, 2, 1);
        bsm.set(true, 1, 16);
        bsm.set(true, 15, 4);
        bsm.set(true, 4, 0);
        bsm.set(true, 9, 4);
        bsm.set(true, 7, 18);
        bsm.set(true, 3, 11);
        bsm.set(true, 19, 11);
        bsm.set(true, 12, 1);
        System.out.println(bsm);
        bsm.spread();
        System.out.println(bsm);
        System.out.println(bsm.split());
    }

    final int n;

    final BooleanBuilder bb;

    BooleanSymmetricMatrix(int n, BooleanBuilder bb) {
        this.n = n;
        this.bb = bb;
    }

    @SuppressWarnings({"SimplifiableIfStatement"})
    public boolean get(int i, int j) {
        if (i > j) {
            int t = i;
            i = j;
            j = t;
        }
        if (i < 0 || j >= n) {
            throw new IllegalArgumentException();
        }
        if (i == j) {
            return true;
        }
        return bb.directGet(j * (j - 1) / 2 + i);
    }

    public void set(boolean x, int i, int j) {
        if (i > j) {
            int t = i;
            i = j;
            j = t;
        }
        if (i < 0 || j >= n) {
            throw new IllegalArgumentException();
        }
        if (i != j) {
            i += j * (j - 1) / 2;
            if (x) {
                bb.directSetT(i);
            } else {
                bb.directSetF(i);
            }
        }
    }

    public BooleanSymmetricMatrix copy() {
        return new BooleanSymmetricMatrix(n, bb.copy());
    }

    /**
     * Assume that i < j
     */
    @SuppressWarnings("SimplifiableIfStatement")
    boolean directGet(int i, int j) {
        return bb.directGet(j * (j - 1) / 2 + i);
    }

    boolean afterOneSpread(int i, int j) {
        for (int k = 0; k < i; k++) {
            if (directGet(k, i) && directGet(k, j)) {
                return true;
            }
        }
        for (int k = i + 1; k < j; k++) {
            if (directGet(i, k) && directGet(k, j)) {
                return true;
            }
        }
        for (int k = j + 1; k < n; k++) {
            if (directGet(i, k) && directGet(j, k)) {
                return true;
            }
        }
        return false;
    }

    public void spread() {
        int c;
        do {
            c = 0;
            for (int j = 1; j < n; j++) {
                for (int i = 0; i < j; i++) {
                    int bi = j * (j - 1) / 2 + i;
                    if (!bb.directGet(bi) && afterOneSpread(i, j)) {
                        bb.directSetT(bi);
                        c++;
                    }
                }
            }
        } while (c > 0);
    }

    public ArraySequence<IntegerArray> split() {
        BooleanBuilder bbn = BooleanBuilder.createFalseWithCapacity(n);
        ArraySequence<IntegerArray> as = new ArraySequence<>(IntegerArray.class);
        for (int j = 0; j < n; j++) {
            if (bbn.directGet(j)) {
                continue;
            }
            IntegerArray ia = new IntegerArray();
            for (int i = 0; i < j; i++) {
                if (directGet(i, j)) {
                    ia.addLast(i);
                    bbn.directSetT(i);
                }
            }
            ia.addLast(j);
            bbn.directSetT(j);
            for (int i = j + 1; i < n; i++) {
                if (directGet(j, i)) {
                    ia.addLast(i);
                    bbn.directSetT(i);
                }
            }
            as.addLast(ia);
        }
        return as;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DsToStringUtil.nameFor(getClass()));
        sb.append(DsToStringUtil.PRIMITIVE_LEFT_BRACKET);
        sb.append(DsToStringUtil.PRIMITIVE_NEW_LINE);
        sb.append(DsToStringUtil.PRIMITIVE_INDENT);
        for (int j = 0; j < n; j++) {
            if (j > 0) {
                sb.append(DsToStringUtil.PRIMITIVE_LINE_SEP);
                sb.append(DsToStringUtil.PRIMITIVE_NEW_LINE);
                sb.append(DsToStringUtil.PRIMITIVE_INDENT);
            }
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    sb.append(DsToStringUtil.PRIMITIVE_ELEMENT_SEP);
                }
                sb.append(get(i, j) ? 1 : 0);
            }
        }
        sb.append(DsToStringUtil.PRIMITIVE_NEW_LINE);
        sb.append(DsToStringUtil.PRIMITIVE_RIGHT_BRACKET);
        return sb.toString();
    }
}
