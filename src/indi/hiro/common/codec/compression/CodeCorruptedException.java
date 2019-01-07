package indi.hiro.common.codec.compression;

/**
 * Created by Hiro on 2018/10/18.
 */
public class CodeCorruptedException extends RuntimeException {

    public static boolean STRICT_MODE = true;

    public CodeCorruptedException() {

    }
}
