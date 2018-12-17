package indi.hiro.common.ds.primitive;

import java.util.Iterator;

/**
 * Created by Hiro on 2018/10/31.
 */
public class StringAsCharacterIterator implements Iterator<Character> {

    private final String s;
    private int p = 0;

    public StringAsCharacterIterator(String s) {
        this.s = s;
    }

    public void reset() {
        p = 0;
    }

    @Override
    public boolean hasNext() {
        return p < s.length();
    }

    @Override
    public Character next() {
        return s.charAt(p++);
    }
}
