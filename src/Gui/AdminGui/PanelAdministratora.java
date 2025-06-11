package Gui.AdminGui;

import Gui.AdminGui.Dodatkowe.BudynekGui;
import Gui.AdminGui.Dodatkowe.OplatyGui;
import Gui.AdminGui.Dodatkowe.PomieszczeniaGui;
import Gui.AdminGui.Dodatkowe.ZgloszeniaGui;
import resources.TworzenieGUI;
import Gui.logowanie;

import javax.swing.*;
import java.awt.*;

public class PanelAdministratora extends JFrame {
    private JPanel PanelAdministratora;
    private JButton budynekButton;
    private JButton pokojButton;
    private JButton wylogujButton;
    private JButton zgloszeniaButton;
    private JButton oplatyButton;



    TworzenieGUI tworzenieGUI = new TworzenieGUI();

    public PanelAdministratora() {
        super("Panel Administratora");
        PanelAdministratora = new JPanel(new BorderLayout(5,5));
        PanelAdministratora.setBackground(Color.WHITE);
        PanelAdministratora.setBorder(BorderFactory.createEmptyBorder());

        panelPrzyciskow();
        ustawienieListenerow();

        this.setContentPane(PanelAdministratora);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void panelPrzyciskow(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        PanelAdministratora.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        budynekButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Budynki", new Color(37, 37, 37),new Color(255,255,255), "/resources/figures/icons8-building2-100.png");
        pokojButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Pomieszczenia", new Color(2, 80, 253),new Color(255,255,255), "/resources/figures/icons8-room-100.png");
        zgloszeniaButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Zgłoszenia", new Color(253, 236, 7),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        oplatyButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Opłaty lokatorów", new Color(253, 236, 7),new Color(255,255,255), "/resources/figures/icons8-building-100.png");
        wylogujButton = tworzenieGUI.tworzeniePrzycisku("Wyloguj", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(budynekButton);
        buttonPanel.add(pokojButton);
        buttonPanel.add(zgloszeniaButton);
        buttonPanel.add(oplatyButton);
        buttonPanel.add(wylogujButton);

        //Panel testowy
        JPanel tablePanel = new JPanel(new GridLayout(5, 1, 15, 0));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        PanelAdministratora.add(tablePanel,BorderLayout.CENTER);

        JButton test = new JButton("Test");
        tablePanel.add(test);

    }

    private void ustawienieListenerow() {
        wylogujButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do logowania");
            new logowanie();
        });

        budynekButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do gui budynku");
            new BudynekGui();
        });
        pokojButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do gui pomieszczeń");
            new PomieszczeniaGui();
        });
        zgloszeniaButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do gui zgloszeń");
            new ZgloszeniaGui();
        });
        oplatyButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do gui opłat");
            new OplatyGui();
        });
    }
}
