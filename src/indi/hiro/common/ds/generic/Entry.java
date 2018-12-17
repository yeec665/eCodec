package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DsToStringUtil;

import java.util.Map;

public class Entry<K, V> implements Map.Entry<K, V> {

    public K k;
    public V v;

    public Entry(K k, V v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V v) {
        V ov = this.v;
        this.v = v;
        return ov;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Map.Entry && k == ((Map.Entry) obj).getKey() && v == ((Map.Entry) obj).getValue();
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DsToStringUtil.nameFor(getClass()));
        sb.append(DsToStringUtil.PAIR_LEFT_BRACKET);
        sb.append(k);
        sb.append(DsToStringUtil.PAIR_ELEMENT_SEP);
        sb.append(v);
        return sb.append(DsToStringUtil.PAIR_RIGHT_BRACKET).toString();
    }
}
