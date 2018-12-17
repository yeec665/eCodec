package indi.hiro.common.math.sym.util;

public class SymCorruptedException extends RuntimeException {

    public static String tryToString(Object o) {
        try {
            return o.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public SymCorruptedException() {
        super();
    }

    public SymCorruptedException(String msg) {
        super(msg);
    }

    public SymCorruptedException(Object corruptedObject) {
        super(tryToString(corruptedObject));
    }
}
