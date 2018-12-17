package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.unknown.Unknown;
import indi.hiro.common.math.sym.ui.TreeToShow;

/**
 * Created by Hiro on 2018/9/21.
 */
public interface Symbolic extends TreeToShow {

    boolean containsUnknown(Unknown u);
}
