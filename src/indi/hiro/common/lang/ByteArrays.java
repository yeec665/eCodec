package indi.hiro.common.lang;

/**
 * Created by Hiro on 2018/11/29.
 */
public class ByteArrays {

    public static final byte[] ZL_ARRAY = new byte[0];

    public static byte[] createFilled(int len, byte content) {
        byte[] bb = new byte[len];
        if (content != 0) {
            for (int i = 0; i < len; i++) {
                bb[i] = content;
            }
        }
        return bb;
    }

    public static byte[] createFilled(int len, byte content, byte head, byte tail) {
        byte[] bb = new byte[len];
        bb[0] = head;
        bb[--len] = tail;
        if (content != 0) {
            for (int i = 1; i < len; i++) {
                bb[i] = content;
            }
        }
        return bb;
    }

    public static byte[] copy1(byte[] original) {
        int n = original.length;
        byte[] replica = new byte[n];
        System.arraycopy(original, 0, replica, 0, n);
        return replica;
    }

    public static byte[][] copy2(byte[][] original) {
        int n = original.length;
        byte[][] replica = new byte[n][];
        for (int i = 0; i < n; i++) {
            replica[i] = copy1(original[i]);
        }
        return replica;
    }

    public static void fill(byte[] src, int srcOff, byte[] dst, int dstOff, int len) {
        System.arraycopy(src, srcOff, dst, dstOff, len);
    }

    public static void fillReverse(byte[] src, int srcOff, byte[] dst, int dstOff, int len) {
        /*
        if (srcOff < 0 || src.length < srcOff + len || dstOff < 0 || dst.length < dstOff + len) {
            throw new IndexOutOfBoundsException();
        }
        */
        for (int i = dstOff + len - 1; i >= dstOff; i--) {
            dst[i] = src[srcOff++];
        }
    }

    public static byte[] copyRepeat(byte[] src, int n) {
        if (n <= 1) {
            if (n == 1) {
                return copy1(src);
            } else {
                return ZL_ARRAY;
            }
        } else {
            int srcLen = src.length;
            if (srcLen == 0) {
                return ZL_ARRAY;
            }
            int dstLen = n * srcLen;
            int lim = dstLen / 2;
            byte[] dst = new byte[dstLen];
            System.arraycopy(src, 0, dst, 0, srcLen);
            do {
                System.arraycopy(dst, 0, dst, srcLen, srcLen);
                srcLen *= 2;
            } while (srcLen <= lim);
            if (srcLen < dstLen) {
                lim = dstLen - srcLen;
                System.arraycopy(dst, 0, dst, srcLen, lim);
            }
            return dst;
        }
    }

    public static void fillRepeat(byte[] src, int srcOff, int srcLen, byte[] dst, int dstOff, int n) {
        if (srcOff < 0 || src.length < srcOff + srcLen) {
            throw new IndexOutOfBoundsException();
        }
        if (srcLen <= 0) {
            if (srcLen == 0) {
                return;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
        if (n <= 1) {
            if (n == 1) {
                System.arraycopy(src, srcOff, dst, dstOff, srcLen);
            }
            return;
        }
        int dstLen = srcLen * n;
        if (dstOff < 0 || dst.length < dstOff + dstLen) {
            throw new IndexOutOfBoundsException();
        }
        int lim = dstLen / 2;
        System.arraycopy(src, srcOff, dst, dstOff, srcLen);
        do {
            System.arraycopy(dst, dstOff, dst, dstOff + srcLen, srcLen);
            srcLen *= 2;
        } while (srcLen <= lim);
        if (srcLen < dstLen) {
            lim = dstLen - srcLen;
            System.arraycopy(dst, dstOff, dst, dstOff + srcLen, lim);
        }
    }
}
