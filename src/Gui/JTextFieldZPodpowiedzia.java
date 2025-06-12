package Gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JTextFieldZPodpowiedzia extends JTextField {
    private final String podpowiedz;
    private boolean pokazPodpowiedz;

    public JTextFieldZPodpowiedzia(final String podpowiedz) {
        this.podpowiedz = podpowiedz;
        this.pokazPodpowiedz = true;
        setText(podpowiedz);
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().isEmpty() || pokazPodpowiedz) {
                    setText("");
                    setForeground(Color.BLACK);
                    pokazPodpowiedz = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(podpowiedz);
                    setForeground(Color.GRAY);
                    pokazPodpowiedz = true;
                }
            }
        });
    }

    @Override
    public String getText() {
        return pokazPodpowiedz ? "" : super.getText();
    }
}
