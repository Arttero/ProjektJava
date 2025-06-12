package Gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TworzeniePrzyciskowGui {
    public JButton tworzeniePrzycisku(String tekst, Color kolor, Color kolorTekstu) {
        JButton przycisk = new JButton(tekst);
        przycisk.setFocusPainted(false);
        przycisk.setBackground(kolor);
        przycisk.setForeground(kolorTekstu);
        przycisk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                przycisk.setBackground(kolor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                przycisk.setBackground(kolor);
            }
        });
        return przycisk;

    }

    public JButton tworzeniePrzyciskuZeZdjeciem(String text, Color color, Color foreground, String icon) {
        JButton przycisk = tworzeniePrzycisku(text, color, foreground);
        przycisk.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(icon))));
        przycisk.setHorizontalTextPosition(SwingConstants.CENTER);
        przycisk.setVerticalTextPosition(SwingConstants.BOTTOM);
        przycisk.setIconTextGap(5);

        return przycisk;
    }
}
