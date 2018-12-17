package indi.hiro.common.math.sym.ui;

import javax.swing.*;
import java.awt.*;

public class FormulaPanel extends JComponent {

    public static void test(ShownOp content) {
        JFrame jFrame = new JFrame(FormulaPanel.class.getSimpleName());
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setLocationRelativeTo(null);
        jFrame.add(new FormulaPanel(content));
        jFrame.setVisible(true);
    }

    private ShownOp content;

    public FormulaPanel(ShownOp content) {
        setContent(content);
    }

    public void setContent(ShownOp content) {
        this.content = content;
        float textSize = content.textSize();
        content.assignSize(textSize, new TextLevelAcc());
        content.assignSize(textSize);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        paint2D(gg);
    }

    public void paint2D(Graphics2D g) {
        content.paint(g, 0.5f * (getWidth() - content.getWidth()),
                0.5f * getHeight());
    }
}
