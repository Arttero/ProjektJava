package Gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GuiCreator {
    public JButton createButton(String text, Color color, Color foreground) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(foreground);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;

    }
    public JButton createIconButton(String text, Color color, Color foreground, String icon) {
        JButton button = createButton(text, color, foreground);
        button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(icon))));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setIconTextGap(5);

        return button;
    }
}
