package indi.hiro.common.ds.util;

import indi.hiro.common.lang.CamelCase;

/**
 * Created by Hiro on 2018/11/24.
 */
public class DsToStringUtil {

    public static String nameFor(Class c) {
        if (DataContainer.class.isAssignableFrom(c)) {
            if (!DataContainer.SpecialContainer.class.isAssignableFrom(c)) {
                return "";
            }
            if (DataContainer.PrimitiveContainer.class.isAssignableFrom(c)) {
                return "";
            }
        }
        return CamelCase.shortClassName(c);
    }

    public static String toString(String prefix, int[] array, int len) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(DsToStringUtil.PRIMITIVE_LEFT_BRACKET);
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(DsToStringUtil.PRIMITIVE_ELEMENT_SEP);
            }
            sb.append(array[i]);
        }
        return sb.append(DsToStringUtil.GENERIC_RIGHT_BRACKET).toString();
    }

    public static final String GENERIC_LEFT_BRACKET = "[";
    public static final String GENERIC_RIGHT_BRACKET = "]";
    public static final String GENERIC_ELEMENT_SEP = ", ";
    public static final String GENERIC_LINE_SEP = "; ";
    public static final String GENERIC_KV_SIGN = " = ";

    public static final String PRIMITIVE_LEFT_BRACKET = "[";
    public static final String PRIMITIVE_RIGHT_BRACKET = "]";
    public static final String PRIMITIVE_ELEMENT_SEP = ", ";
    public static final String PRIMITIVE_LINE_SEP = ";";
    public static final String PRIMITIVE_KV_SIGN = " = ";
    public static final String PRIMITIVE_NEW_LINE = "\r\n";
    public static final String PRIMITIVE_INDENT = "  ";

    public static final String SPECIAL_LEFT_BRACKET = "{";
    public static final String SPECIAL_RIGHT_BRACKET = "}";

    public static final String PAIR_LEFT_BRACKET = "<";
    public static final String PAIR_RIGHT_BRACKET = ">";
    public static final String PAIR_ELEMENT_SEP = ", ";
}
