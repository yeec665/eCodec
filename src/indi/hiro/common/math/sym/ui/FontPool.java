package indi.hiro.common.math.sym.ui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

@SuppressWarnings("Java8MapApi")
public class FontPool {

    public static final String FONT_NAME = "Cambria Math";
    public static final AffineTransform IDENTITY_AT = new AffineTransform();
    public static final FontRenderContext FRC = new FontRenderContext(IDENTITY_AT, true, true);

    private static final HashMap<Integer, Font> PLAIN_FONT_MAP = new HashMap<>();
    private static final HashMap<Integer, Font> ITALIC_FONT_MAP = new HashMap<>();

    public static Font getPlain(int size) {
        Font font = PLAIN_FONT_MAP.get(size);
        if (font == null) {
            font = new Font(FONT_NAME, Font.PLAIN, size);
            PLAIN_FONT_MAP.put(size, font);
        }
        return font;
    }

    public static Font getPlain(float size) {
        return getPlain((int) (size + 0.5f));
    }

    public static Font getItalic(int size) {
        Font font = ITALIC_FONT_MAP.get(size);
        if (font == null) {
            font = new Font(FONT_NAME, Font.ITALIC, size);
            ITALIC_FONT_MAP.put(size, font);
        }
        return font;
    }

    public static Font getItalic(float size) {
        return getItalic((int) (size + 0.5f));
    }
}
