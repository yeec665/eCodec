package indi.hiro.common.lang;

/**
 * Created by Hiro on 2018/12/6.
 *
 * For coordinates (x, y)
 */
public class DoublePairArrays {

    public static void copy(double[] src, int srcOff, double[] dst, int dstOff, int nPt, boolean reverse) {
        if (reverse) {
            dstOff += 2 * (nPt - 1);
            while (nPt-- > 0) {
                dst[dstOff] = src[srcOff];
                dst[dstOff + 1] = src[srcOff + 1];
                srcOff += 2;
                dstOff -= 2;
            }
        } else {
            System.arraycopy(src, srcOff, dst, dstOff, 2 * nPt);
        }
    }

    public static void copy(double[] src, int srcOff, double[] dst, int dstOff, int nPt, boolean reverse, boolean xNegate, boolean yNegate) {
        if (!(xNegate || yNegate)) {
            copy(src, srcOff, dst, dstOff, nPt, reverse);
        } else if (reverse) {
            dstOff += 2 * (nPt - 1);
            while (nPt-- > 0) {
                dst[dstOff] = xNegate ? -src[srcOff] : src[srcOff];
                dst[dstOff + 1] = yNegate ? -src[srcOff + 1] : src[srcOff + 1];
                srcOff += 2;
                dstOff -= 2;
            }
        } else {
            double v;
            while (nPt-- > 0) {
                v = src[srcOff++];
                dst[dstOff++] = xNegate ? -v : v;
                v = src[srcOff++];
                dst[dstOff++] = yNegate ? -v : v;
            }
        }
    }
}
