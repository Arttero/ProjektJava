package Gui.AdminGui;

import Gui.GuiCreator;
import Gui.logowanie;

import javax.swing.*;
import java.awt.*;

public class PanelAdministratora extends JFrame {
    private JPanel PanelAdministratora;
    private JButton budynekButton;
    private JButton pokojButton;
    private JButton testButton;
    private JButton wylogujButton;



    GuiCreator guiCreator = new GuiCreator();

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
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        PanelAdministratora.add(buttonPanel,BorderLayout.WEST);
        budynekButton = guiCreator.createIconButton("Budynek", new Color(37, 37, 37),new Color(255,255,255), "/figures/icons8-building2-100.png");
        pokojButton = guiCreator.createIconButton("Pokoj", new Color(2, 80, 253),new Color(255,255,255), "/figures/icons8-building-100.png");
        testButton = guiCreator.createIconButton("Test", new Color(253, 236, 7),new Color(255,255,255), "/figures/icons8-building-100.png");
        wylogujButton = guiCreator.createButton("Wyloguj", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(budynekButton);
        buttonPanel.add(pokojButton);
        buttonPanel.add(testButton);
        buttonPanel.add(wylogujButton);

        //panel testowy
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
    }
}
