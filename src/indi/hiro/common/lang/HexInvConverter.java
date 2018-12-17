package indi.hiro.common.lang;

public class HexInvConverter {

    private static int toVHex(int c) throws NumberFormatException {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'F') {
            return c + (0xA - 'A');
        } else if (c >= 'a' && c <= 'f') {
            return c + (0xa - 'a');
        } else {
            throw new NumberFormatException();
        }
    }

    public byte[] toByteArray(String s) throws NumberFormatException {
        byte[] bb = new byte[(s.length() + 1) / 2];
        for (int i = 0, j = 0; i < bb.length; i++) {
            bb[i] = (byte) ((toVHex(s.charAt(j++)) << 4) | toVHex(s.charAt(j++)));
        }
        return bb;
    }
}
