package indi.hiro.common.math.sym.real;

/**
 * Created by Hiro on 2018/9/21.
 */
public interface Irrational extends Real {

    @Override
    default int rational() {
        return R_IRRATIONAL;
    }

}
