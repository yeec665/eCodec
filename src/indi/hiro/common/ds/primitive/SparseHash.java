package indi.hiro.common.ds.primitive;

import indi.hiro.common.lang.HexConverter;

/**
 * Created by Hiro on 2018/11/1.
 */
public class SparseHash {

    public static void classTest() {
        StringBuilder sb = new StringBuilder();
        SparseHash sparseHash = new SparseHash(4);
        for (int i = 0; i < 100; i++) {
            HexConverter.appendHexTo(sb, sparseHash.next());
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
    }

    private final int[] gaps;

    public SparseHash(int k) {
        gaps = new int[k];
    }

    public synchronized int next() {
        int g = 0, x = 0;
        for (int i : gaps) {
            g += i;
            x |= (1 << g++);
        }
        if (g < Integer.SIZE) {
            gaps[0]++;
        } else {
            int i = 0;
            while (gaps[i] == 0) {
                i++;
            }
            gaps[i++] = 0;
            if (i < gaps.length) {
                gaps[i]++;
            }
        }
        return x;
    }
}
