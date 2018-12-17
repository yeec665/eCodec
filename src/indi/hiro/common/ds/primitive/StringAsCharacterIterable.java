package indi.hiro.common.ds.primitive;

import java.util.Iterator;

/**
 * Created by Hiro on 2018/10/31.
 */
public class StringAsCharacterIterable implements Iterable<Character> {

    private final String s;

    public StringAsCharacterIterable(String s) {
        this.s = s;
    }

    @Override
    public Iterator<Character> iterator() {
        return new StringAsCharacterIterator(s);
    }
}
