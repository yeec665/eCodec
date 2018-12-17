package indi.hiro.common.lang;

import indi.hiro.common.ds.primitive.ByteConsumer;
import indi.hiro.common.ds.primitive.FloatConsumer;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class WrappedByteConsumer implements ByteConsumer, IntConsumer, FloatConsumer,
        LongConsumer, DoubleConsumer, Consumer<String> {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private final ByteConsumer bc;

    public WrappedByteConsumer(ByteConsumer bc) {
        this.bc = bc;
    }

    public void acceptBoolean(boolean v) {
        bc.accept((byte) (v ? 1 : 0));
    }

    @Override
    public void accept(byte v) {
        bc.accept(v);
    }

    public void accept(char v) {
        bc.accept((byte) (v >>> 8));
        bc.accept((byte) v);
    }

    public void accept(short v) {
        bc.accept((byte) (v >>> 8));
        bc.accept((byte) v);
    }

    @Override
    public void accept(int v) {
        bc.accept((byte) (v >>> 24));
        bc.accept((byte) (v >>> 16));
        bc.accept((byte) (v >>> 8));
        bc.accept((byte) v);
    }

    @Override
    public void accept(float v) {
        accept(Float.floatToIntBits(v));
    }

    @Override
    public void accept(long v) {
        bc.accept((byte) (v >>> 56));
        bc.accept((byte) (v >>> 48));
        bc.accept((byte) (v >>> 40));
        bc.accept((byte) (v >>> 32));
        bc.accept((byte) (v >>> 24));
        bc.accept((byte) (v >>> 16));
        bc.accept((byte) (v >>> 8));
        bc.accept((byte) v);
    }

    @Override
    public void accept(double v) {
        accept(Double.doubleToLongBits(v));
    }

    @Override
    public void accept(String v) {
        acceptArray(v.getBytes(CHARSET));
    }

    public void accept(List<String> v) {
        for (String s : v) {
            accept(s);
        }
    }

    public void accept(Set<String> v) {
        List<String> sList = new ArrayList<>(v);
        sList.sort(String::compareTo);
        accept(sList);
    }

    public void accept(HashMap<String, String> v) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(v.entrySet());
        entryList.sort(Comparator.comparing(Map.Entry::getKey, String::compareTo));
        for (Map.Entry<String, String> entry : entryList) {
            accept(entry.getKey());
            accept(entry.getValue());
        }
    }

    public static class MessageDigestAdaptor implements ByteConsumer {

        private final MessageDigest md;

        public MessageDigestAdaptor(MessageDigest md) {
            this.md = md;
        }

        public MessageDigestAdaptor() throws GeneralSecurityException {
            this.md = MessageDigest.getInstance("SHA-256");
        }

        @Override
        public void accept(byte v) {
            md.update(v);
        }

        @Override
        public void acceptArray(byte[] v) {
            md.update(v);
        }

        @Override
        public void acceptArray(byte[] value, int off, int len) {
            md.update(value, off, len);
        }

        public MessageDigest getMessageDigest() {
            return md;
        }

        public byte[] resultAsBytes() {
            return md.digest();
        }

        public void appendHexString(StringBuilder sb) {
            HexConverter.appendHexTo(sb, md.digest());
        }

        public String toHexString() {
            return HexConverter.toHex("", md.digest());
        }
    }
}
