package indi.hiro.common.lang;

import java.util.function.IntPredicate;

public class BooleanArrays {

    public static boolean[] filledArray(int len, boolean b) {
        boolean[] bb = new boolean[len];
        if (b) {
            for (int i = 0; i < len; i++) {
                bb[i] = true;
            }
        }
        return bb;
    }

    public static boolean[] filledArray(int len, IntPredicate predicate) {
        boolean[] bb = new boolean[len];
        for (int i = 0; i < len; i++) {
            bb[i] = predicate.test(i);
        }
        return bb;
    }
}
