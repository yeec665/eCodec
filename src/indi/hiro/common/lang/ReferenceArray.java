package indi.hiro.common.lang;

public class ReferenceArray {

    public static int countNull(Object[] array) {
        int n = 0;
        for (Object obj : array) {
            if (obj == null) {
                n++;
            }
        }
        return n;
    }

    public static int countNonNull(Object[] array) {
        int n = 0;
        for (Object obj : array) {
            if (obj != null) {
                n++;
            }
        }
        return n;
    }
}
