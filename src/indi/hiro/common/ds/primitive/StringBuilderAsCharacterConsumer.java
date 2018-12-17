package indi.hiro.common.ds.primitive;

import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/10/30.
 */
public class StringBuilderAsCharacterConsumer implements Consumer<Character> {

    private StringBuilder sb;

    public StringBuilderAsCharacterConsumer() {
        sb = new StringBuilder();
    }

    public StringBuilderAsCharacterConsumer(String s) {
        sb = new StringBuilder(s);
    }

    public StringBuilderAsCharacterConsumer(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void accept(Character c) {
        sb.append((char) c);
    }
}
