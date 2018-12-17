package indi.hiro.common.math.sym.unknown;

import indi.hiro.common.math.sym.function.Expression;
import indi.hiro.common.math.sym.nBasic.BcdParser;
import indi.hiro.common.math.sym.nBasic.IntOp;
import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownRearScript;
import indi.hiro.common.math.sym.ui.ShownString;

public class Unknown implements Expression {

    final String name, subScript;

    public Unknown(String name, String subScript) {
        this.name = name;
        this.subScript = subScript;
    }

    public Unknown(String name) {
        this.name = name;
        this.subScript = null;
    }

    @Override
    public boolean containsUnknown(Unknown u) {
        return this == u;
    }

    @Override
    public int constant() {
        return C_VAR;
    }

    public boolean hasSubscript() {
        return subScript != null && subScript.length() > 0;
    }

    public int uCompare(Unknown that) {
        int c = name.compareTo(that.name);
        if (c == 0) {
            return CompareSub(that);
        } else {
            return c;
        }
    }

    private int CompareSub(Unknown that) {
        if (hasSubscript()) {
            if (that.hasSubscript()) {
                return compareSubString(this.subScript, that.subScript);
            } else {
                return 1;
            }
        } else {
            if (that.hasSubscript()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private int compareSubString(String s1, String s2) {
        if (BcdParser.decParseCheck(s1)) {
            if (BcdParser.decParseCheck(s2)) {
                return IntOp.compare(BcdParser.parseBcd(s1), BcdParser.parseBcd(s2));
            } else {
                return -1;
            }
        } else {
            if (BcdParser.decParseCheck(s2)) {
                return 1;
            } else {
                return s1.compareTo(s2);
            }
        }
    }

    @Override
    public int order(int i) {
        if (i == 0) {
            return 21;
        } else if (i == 1) {
            return name.length();
        } else if (i == 2) {
            return subScript != null ? subScript.length() : 0;
        } else {
            i -= 3;
            if (i < name.length()) {
                return name.charAt(i);
            }
            i -= name.length();
            if (subScript != null && i < subScript.length()) {
                return subScript.charAt(i);
            }
            return -1;
        }
    }

    @Override
    public int orderLength() {
        int len = 3 + name.length();
        if (subScript != null) {
            len += subScript.length();
        }
        return len;
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append(name);
        if (hasSubscript()) {
            sb.append('_');
            sb.append(subScript);
        }
    }

    @Override
    public ShownOp show() {
        if (hasSubscript()) {
            return new ShownRearScript(new ShownString(name), null, new ShownString(subScript));
        } else {
            return new ShownString(name);
        }
    }
}
