package indi.hiro.common.lang;

public class HexConverter {

    private static void appendVHex(StringBuilder sb, int vHex) {
        if (vHex < 0xA) {
            sb.append((char) ('0' + vHex));
        } else {
            sb.append((char) ('A' - 0xA + vHex));
        }
    }

    public static void appendHexTo(StringBuilder sb, byte b) {
        appendVHex(sb, 0xF & (b >>> 4));
        appendVHex(sb, 0xF & b);
    }

    public static void appendHexTo(StringBuilder sb, byte[] bb) {
        for (byte b : bb) {
            appendHexTo(sb, b);
        }
    }

    public static String toHex(String prefix, byte[] bb) {
        StringBuilder sb = new StringBuilder(prefix);
        appendHexTo(sb, bb);
        return sb.toString();
    }

    public static void appendHexTo(StringBuilder sb, char c) {
        for (int i = 3; i >= 0; i--) {
            appendVHex(sb, 0xF & (c >>> (i << 2)));
        }
    }

    public static void appendHexTo(StringBuilder sb, int v) {
        for (int i = 7; i >= 0; i--) {
            appendVHex(sb, 0xF & (v >>> (i << 2)));
        }
    }

    public static String toHex(String prefix, int v) {
        StringBuilder sb = new StringBuilder(prefix);
        appendHexTo(sb, v);
        return sb.toString();
    }

    public static void appendHexTo(StringBuilder sb, long v) {
        for (int i = 15; i >= 0; i--) {
            long vHex = 0xF & (v >>> (i << 2));
            if (vHex < 0xA) {
                sb.append((char) ('0' + vHex));
            } else {
                sb.append((char) ('A' - 0xA + vHex));
            }
        }
    }

    public static String toHex(String prefix, long v) {
        StringBuilder sb = new StringBuilder(prefix);
        appendHexTo(sb, v);
        return sb.toString();
    }

    private String prefix = "0x";

    private String suffix = "";

    private boolean fullLength = true;

    private boolean upperCase = true;

    public HexConverter() {

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean getFullLength() {
        return fullLength;
    }

    public void setFullLength(boolean fullLength) {
        this.fullLength = fullLength;
    }

    public boolean getUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }

    public String convert(int v) {
        StringBuilder sb = new StringBuilder(prefix);
        boolean started = fullLength;
        for (int i = 7; i >= 0; i--) {
            int x = 0xF & (v >> (i * 4));
            if (!started && x == 0) {
                continue;
            }
            started = true;
            if (x < 0xA) {
                sb.append((char) ('0' + x));
            } else if (upperCase) {
                sb.append((char) ('A' - 0xA + x));
            } else {
                sb.append((char) ('a' - 0xA + x));
            }
        }
        sb.append(suffix);
        return sb.toString();
    }

    public String convert(long v) {
        StringBuilder sb = new StringBuilder(prefix);
        boolean started = fullLength;
        for (int i = 15; i >= 0; i--) {
            int x = 0xF & (int) (v >> (i * 4));
            if (!started && x == 0) {
                continue;
            }
            started = true;
            if (x < 0xA) {
                sb.append((char) ('0' + x));
            } else if (upperCase) {
                sb.append((char) ('A' - 0xA + x));
            } else {
                sb.append((char) ('a' - 0xA + x));
            }
        }
        sb.append(suffix);
        return sb.toString();
    }
}
