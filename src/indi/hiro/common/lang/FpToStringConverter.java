package indi.hiro.common.lang;

import sun.misc.FormattedFloatingDecimal;

public class FpToStringConverter {

    public static void appendDec(StringBuilder sb, double d, int precision) {
        FormattedFloatingDecimal fd = FormattedFloatingDecimal.valueOf(d, precision, FormattedFloatingDecimal.Form.DECIMAL_FLOAT);
        sb.append(fd.getMantissa());
    }

    public static String toDecString(double d, int precision) {
        FormattedFloatingDecimal fd = FormattedFloatingDecimal.valueOf(d, precision, FormattedFloatingDecimal.Form.DECIMAL_FLOAT);
        return new String(fd.getMantissa());
    }

    public static String toSciString(double d, int precision) {
        FormattedFloatingDecimal fd = FormattedFloatingDecimal.valueOf(d, precision, FormattedFloatingDecimal.Form.SCIENTIFIC);
        return new String(fd.getMantissa());
    }

    public static void append(StringBuilder sb, double d, int precision) {
        FormattedFloatingDecimal fd = FormattedFloatingDecimal.valueOf(d, precision, FormattedFloatingDecimal.Form.COMPATIBLE);
        sb.append(fd.getMantissa());
    }

    public static String toString(double d, int precision) {
        FormattedFloatingDecimal fd = FormattedFloatingDecimal.valueOf(d, precision, FormattedFloatingDecimal.Form.COMPATIBLE);
        return new String(fd.getMantissa());
    }

    public static void appendArray(StringBuilder sb, double[] dd, int precision) {
        sb.append('[');
        for (int i = 0, len = dd.length; i < len; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            appendDec(sb, dd[i], precision);
        }
        sb.append(']');
    }

    public static void appendArray(StringBuilder sb, float[] ff, int precision) {
        sb.append('[');
        for (int i = 0, len = ff.length; i < len; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            appendDec(sb, ff[i], precision);
        }
        sb.append(']');
    }

    public static String arrayToString(double[] dd, int precision) {
        StringBuilder sb = new StringBuilder();
        appendArray(sb, dd, precision);
        return sb.toString();
    }

    public static String arrayToString(String prefix, double[] dd, int precision) {
        StringBuilder sb = new StringBuilder(prefix);
        appendArray(sb, dd, precision);
        return sb.toString();
    }
}
