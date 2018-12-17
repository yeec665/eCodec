package indi.hiro.common.ds.theoretical;

import indi.hiro.common.ds.primitive.CharCounter;
import indi.hiro.common.ds.primitive.IntCounter;

import java.util.function.IntConsumer;

/**
 * Created by Hiro on 2018/11/6.
 */
public class InformationEntropy {

    private static final double LN_2 = 0.69314718055995; // Math.log(2.0);

    public static void classTest() {
        IntCounter ic = new IntCounter();
        ic.add(0, 4);
        ic.add(1, 3);
        ic.add(2, 1);
        ic.add(3, 1);
        System.out.println(ic);
        System.out.println(entropy(ic));
    }

    public static double entropy(CharCounter ic) {
        EntropyCalculator ec = new EntropyCalculator();
        ic.iterateI(ec);
        return ec.entropy();
    }

    public static double entropy(IntCounter ic) {
        EntropyCalculator ec = new EntropyCalculator();
        ic.iterateI(ec);
        return ec.entropy();
    }

    private static class EntropyCalculator implements IntConsumer {

        private double sv = 0, svLog = 0;

        @Override
        public void accept(int v) {
            sv += v;
            svLog += v * Math.log(v);
        }

        public double entropy() {
            return 1.0 / (sv * LN_2) * (Math.log(sv) * sv - svLog);
        }
    }
}
