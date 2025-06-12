package Gui.UserGui;

import Gui.TworzeniePrzyciskowGui;

import javax.swing.*;
import java.awt.*;

public class PanelUzytkownika extends JFrame{
    private JPanel PanelUzytkownika;
    private JButton listaPosiadanychPokoiButton;
    private JButton wylogujButton;
    private JButton oplatyButton;
    private JButton zgloszeniaButton;







    TworzeniePrzyciskowGui tworzeniePrzyciskow = new TworzeniePrzyciskowGui();
    //UserDAO userDAO = new UserDAO();

    public PanelUzytkownika() {
        super("Panel Uzytkownika");
        PanelUzytkownika = new JPanel(new BorderLayout(10,10));
        PanelUzytkownika.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panelZNapisem();
//        panelZDodawaniemUzytkownika();
//        panelDolny();
//        ustawienieListenerow();

        this.setContentPane(PanelUzytkownika);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void panelZNapisem() {
        JPanel panelNapis = new JPanel();
        panelNapis.setLayout(new BoxLayout(panelNapis,BoxLayout.X_AXIS));
        panelNapis.setBackground(Color.WHITE);
        panelNapis.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-user-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);

        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("PANEL UŻYTKOWNIKA");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 50));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        panelNapis.add(zdjecieLabel);
        panelNapis.add(tekstLabel);


        PanelUzytkownika.add(panelNapis,BorderLayout.NORTH);
    }
}
