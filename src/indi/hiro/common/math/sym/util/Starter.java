package indi.hiro.common.math.sym.util;

import indi.hiro.common.math.sym.nBasic.IntOpTester;
import indi.hiro.common.math.sym.real.RealOpTester;
import indi.hiro.common.math.sym.ui.*;

public class Starter {

    public static void main(String[] args) {
        (new IntOpTester()).syntheticTest();
        RealOpTester.test();
    }

    public static void testFormula() {
        ShownArray f1 = new ShownArray(new ShownString("interface"));
        f1.addLast(new ShownBracket(new ShownString("w"), 7));
        f1.addLast(new ShownSymbol(6));
        f1.addLast(new ShownBracket(new ShownString("j"), 8));
        f1.addLast(new ShownSymbol(7));
        f1.addLast(new ShownBracket(new ShownString("f"), 9));
        f1.addLast(new ShownSymbol(8));
        f1.addLast(new ShownBracket(new ShownString("eyp"), 7));
        f1.addLast(new ShownSymbol(3));
        f1.addLast(new ShownBracket(new ShownString("edp"), 8));
        f1.addLast(new ShownSymbol(4));
        f1.addLast(new ShownBracket(new ShownString("ekp"), 9));
        ShownMatrix f2 = new ShownMatrix(3, 3);
        f2.setSop(new ShownString("2333"), 0, 0);
        f2.setSop(new ShownFractionLine(new ShownString("4"), new ShownString("5")), 1, 0);
        f2.setSop(new ShownSymbol(ShownSymbol.SYMBOL_MULTIPLY_6_HOZ), 0, 1);
        f2.setSop(new ShownSymbol(ShownSymbol.SYMBOL_EQUAL_3), 1, 1);
        f2.setSop(new ShownHat(new ShownString("use"), ShownHat.HAT_STYLE_ARROW_R), 2, 1);
        f2.setSop(new ShownHat(new ShownString("f2"), ShownHat.HAT_STYLE_SIMPLE_ARROW_LR), 0, 2);
        f2.setSop(new ShownBracket(new ShownString("ppp"), ShownBracket.BRACKET_STYLE_CURLY), 2, 2);
        FormulaPanel.test(new ShownFractionLine(new ShownBracket(f2, ShownBracket.BRACKET_STYLE_CURLY), f1));
    }
}
