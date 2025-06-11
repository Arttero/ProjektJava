package Gui.AdminGui.Dodatkowe;

import javax.swing.*;
import java.awt.*;

public class InformacjeOPokojuGui extends JFrame {


    public InformacjeOPokojuGui(String imie, String nazwisko, String pesel, String najblizszaZalpata, String ostatniaZaplata) {
        setTitle("Informacje o lokatorze");
        setSize(600,500);
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(new JLabel("Imie:"));
        panel.add(new JLabel(imie));

        panel.add(new JLabel("Nazwisko:"));
        panel.add(new JLabel(nazwisko));

        panel.add(new JLabel("Pesel:"));
        panel.add(new JLabel(pesel));

        panel.add(new JLabel("NajblizszaZalpata:"));
        panel.add(new JLabel(najblizszaZalpata));

        panel.add(new JLabel("OstatniaZaplata:"));
        panel.add(new JLabel(ostatniaZaplata));

        JButton edytujButton = new JButton("Edytuj dane");
        edytujButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"Test","Test",JOptionPane.INFORMATION_MESSAGE);
        });

        add(panel,BorderLayout.CENTER);
        add(edytujButton,BorderLayout.SOUTH);
        setVisible(true);
    }
}
