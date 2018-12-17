package indi.hiro.common.math.sym.real;

/**
 * Created by Hiro on 2018/9/24.
 */
public interface RealOpConfig {

    int PREFER_BCD_INT =        0x00000001;
    int FORCE_USE_INT =         0x00000002;

    int PREFER_FD =             0x00000010;
    int FORCE_USE_RATIONAL =    0x00000020;

    int ALLOW_INT_FD =          0x00000100;
    int ALLOW_INT_FR =          0x00000200;

    int FORCE_ZZXCF =           0x00001000;
    int FORCE_GXJSS =          0x00002000;
}
