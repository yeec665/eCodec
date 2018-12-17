package indi.hiro.common.lang;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Iterator;

/**
 * Created by Hiro on 2018/11/24.
 */
public class CamelCase {

    public static boolean isLowerCase(char ch) {
        return 'a' <= ch && ch <= 'z';
    }

    public static boolean isUpperCase(char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

    public static String shortClassName(@NotNull Class c) {
        String name = c.getSimpleName();
        int len = name.length();
        if (len <= 16) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(name.charAt(0));
        for (int i = 1; i < len; i++) {
            char ch = name.charAt(i);
            if (isUpperCase(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static boolean allUpperCase(@NotNull String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String camelCombine(@NotNull Iterable<String> strings, boolean firstUpperCase) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            if (s == null || s.length() == 0) {
                continue;
            }
            if (!firstUpperCase) {
                firstUpperCase = true;
                char ch = s.charAt(0);
                if (isUpperCase(ch)) {
                    if (allUpperCase(s)) {
                        sb.append(s.toLowerCase());
                    } else {
                        sb.append((char) ('a' - 'A' + ch));
                        sb.append(s, 1, s.length());
                    }
                } else {
                    sb.append(s);
                }
            } else {
                char ch = s.charAt(0);
                if (isLowerCase(ch)) {
                    sb.append((char) ('A' - 'a' + ch));
                    sb.append(s, 1, s.length());
                } else {
                    sb.append(s);
                }
            }
        }
        return sb.toString();
    }

    public static String[] camelSplit(String string) {
        // TODO
        return null;
    }
}
