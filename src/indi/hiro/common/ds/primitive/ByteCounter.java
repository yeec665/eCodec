package indi.hiro.common.ds.primitive;

import indi.hiro.common.codec.compression.HuffmanTreeNode;
import indi.hiro.common.ds.generic.Entry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ByteCounter {

    private final int[] fn = new int[0x100];

    public ByteCounter() {

    }

    public int size() {
        int nzn = 0;
        for (int n : fn) {
            if (n != 0) {
                nzn++;
            }
        }
        return nzn;
    }

    public void iterateI(IntConsumer ic) {
        for (int n : fn) {
            if (n != 0) {
                ic.accept(n);
            }
        }
    }

    public void iterateII(ByteIntConsumer bic) {
        for (int i = 0; i < 0x100; i++) {
            int n = fn[i];
            if (n != 0) {
                bic.accept((byte) i, n);
            }
        }
    }

    public void iterateIP(Consumer<ByteIntPair> ipc) {
        for (int i = 0; i < 0x100; i++) {
            int n = fn[i];
            if (n != 0) {
                ipc.accept(new ByteIntPair((byte) i, n));
            }
        }
    }

    public int count(byte x) {
        return fn[0xFF & x];
    }

    public int countAll() {
        int sum = 0;
        for (int n : fn) {
            sum += n;
        }
        return sum;
    }

    public void addOne(byte x) {
        fn[0xFF & x]++;
    }

    public void minusOne(byte x) {
        fn[0xFF & x] = Math.max(0, fn[0xFF & x] - 1);
    }

    public void add(byte[] xx) {
        for (byte x : xx) {
            fn[0xFF & x]++;
        }
    }

    public void add(byte[] xx, int off, int len) {
        for (int lim = off + len; off < lim; off++) {
            fn[0xFF & xx[off]]++;
        }
    }

    public void add(byte x, int n) {
        if (n > 0) {
            fn[0xFF & x] += n;
        } else if (n < 0) {
            fn[0xFF & x] = Math.max(0, fn[0xFF & x] + n);
        }
    }

    public int addAndCount(byte x, int n) {
        if (n < 0) {
            return fn[0xFF & x] = Math.max(0, fn[0xFF & x] + n);
        } else {
            return fn[0xFF & x] += n;
        }
    }

    public HashMap<Byte, Integer> toMap() {
        HashMap<Byte, Integer> map = new HashMap<>();
        for (int x = 0; x < 0x100; x++) {
            if (fn[x] != 0) {
                map.put((byte) x, fn[x]);
            }
        }
        return map;
    }

    public ArrayList<Entry<Byte, Integer>> toSortedList() {
        ArrayList<Entry<Byte, Integer>> list = new ArrayList<>();
        for (int x = 0; x < 0x100; x++) {
            if (fn[x] != 0) {
                list.add(new Entry<>((byte) x, fn[x]));
            }
        }
        if (list.size() > 1) {
            list.sort(Comparator.comparingInt(x -> x.v));
        }
        return list;
    }

    public ArrayList<HuffmanTreeNode<Byte>> toHuffmanSegments() {
        ArrayList<HuffmanTreeNode<Byte>> list = new ArrayList<>();
        for (int x = 0; x < 0x100; x++) {
            if (fn[x] != 0) {
                list.add(new HuffmanTreeNode<>((byte) x, fn[x]));
            }
        }
        return list;
    }

    public static class ByteIntPair {
        public byte a;
        public int b;

        public ByteIntPair(byte a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    @FunctionalInterface
    public interface ByteIntConsumer {

        void accept(byte a, int b);
    }
}
