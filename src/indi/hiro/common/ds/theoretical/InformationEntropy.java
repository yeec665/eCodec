package indi.hiro.common.ds.theoretical;

import indi.hiro.common.ds.primitive.BooleanBuilder;
import indi.hiro.common.ds.primitive.CharCounter;
import indi.hiro.common.ds.primitive.IntCounter;

import java.util.ArrayList;
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

    public static double entropy(CharCounter cc) {
        EntropyCalculator ec = new EntropyCalculator(cc.countAll());
        cc.iterateI(ec);
        return ec.entropy();
    }

    public static double entropy(IntCounter ic) {
        EntropyCalculator ec = new EntropyCalculator(ic.countAll());
        ic.iterateI(ec);
        return ec.entropy();
    }

    public static double entropy(String s) {
        return entropy(new CharCounter(s));
    }

    public static double entropy(ArrayList<Integer> integerArrayList) {
        IntCounter intCounter = new IntCounter();
        for (int i : integerArrayList) {
            intCounter.addOne(i);
        }
        return entropy(intCounter);
    }

    public static double entropy(BooleanBuilder bb) {
        int n = bb.bitLen(), c = 0;
        for (int i = 0; i < n; i++) {
            if (bb.get(i)) {
                c++;
            }
        }
        return entropyBin((double) c / n);
    }

    public static double entropyBin(double p) {
        return -(p * Math.log(p) + (1.0 - p) * Math.log(1.0 - p));
    }

    private static class EntropyCalculator implements IntConsumer {

        private double sv = 0, svLog = 0;

        EntropyCalculator(double sv) {
            this.sv = sv;
        }

        @Override
        public void accept(int v) {
            double dv = v / sv;
            svLog += dv * Math.log(dv);
        }

        public double entropy() {
            return -svLog / LN_2;
        }
    }
}
