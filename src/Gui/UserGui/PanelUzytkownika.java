package Gui.UserGui;

import Gui.TworzeniePrzyciskowGui;
import Gui.UserGui.Dodatkowe.OplatyUserGui;
import Gui.UserGui.Dodatkowe.PosiadanePomieszczeniaGui;
import Gui.UserGui.Dodatkowe.ZmianaDanychGui;
import Gui.logowanie;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class PanelUzytkownika extends JFrame{
    private JPanel panelUzytkownika;
    private JPanel panelSrodkowy;
    private JButton listaPosiadanychPokoiButton;
    private JButton wylogujButton;
    private JButton oplatyButton;;
    private JButton zmianaDanychButton;
    private int idUzytkownika;
    private Connection connection;

    TworzeniePrzyciskowGui tworzeniePrzyciskow = new TworzeniePrzyciskowGui();

    public PanelUzytkownika(int idUzytkownika, Connection connection) {
        super("Panel Użytkownika");
        this.idUzytkownika = idUzytkownika;
        this.connection = connection;
        panelUzytkownika = new JPanel(new BorderLayout(10,10));
        panelUzytkownika.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panelUzytkownika.setBackground(Color.LIGHT_GRAY);

        panelZNapisem();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(panelUzytkownika);
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


        panelUzytkownika.add(panelNapis,BorderLayout.NORTH);
    }

    private void panelSrodkowy() {
        panelSrodkowy = new JPanel();
        panelSrodkowy.setBackground(Color.WHITE);
        panelSrodkowy.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 40)); // środek panelu

        JLabel zegarLabel = new JLabel();
        zegarLabel.setFont(new Font("Arial", Font.BOLD, 36));
        zegarLabel.setForeground(Color.BLACK);
        panelSrodkowy.add(zegarLabel);

        //timer odświeżający godzinę co pół sekundy
        Timer timer = new Timer(500, e -> {
            zegarLabel.setText("Godzina: " + java.time.LocalTime.now().withNano(0).toString());
        });
        timer.setInitialDelay(0);
        timer.start();

        panelUzytkownika.add(panelSrodkowy, BorderLayout.CENTER);

    }

    private void panelDolny(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        panelUzytkownika.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        oplatyButton = tworzeniePrzyciskow.tworzeniePrzyciskuZeZdjeciem("Opłaty", new Color(5, 189, 13),new Color(255,255,255), "/resources/icons/icons8-money-100.png");
        listaPosiadanychPokoiButton = tworzeniePrzyciskow.tworzeniePrzyciskuZeZdjeciem("Posiadanie pomieszczenia", new Color(2, 80, 253),new Color(255,255,255), "/resources/icons/icons8-room-100.png");
        zmianaDanychButton = tworzeniePrzyciskow.tworzeniePrzyciskuZeZdjeciem("Zmień dane osobowe", new Color(146, 75, 250),new Color(255,255,255), "/resources/icons/icons8-user-100.png");
        wylogujButton = tworzeniePrzyciskow.tworzeniePrzycisku("Wyloguj", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(oplatyButton);
        buttonPanel.add(listaPosiadanychPokoiButton);
        buttonPanel.add(zmianaDanychButton);
        buttonPanel.add(wylogujButton);
    }

    private void ustawienieListenerow() {
        wylogujButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do logowania");
            new logowanie();
        });
        oplatyButton.addActionListener(e -> {
            dispose();
            System.out.println("Przechodzę do opłat");
            new OplatyUserGui(idUzytkownika, connection);
        });
        listaPosiadanychPokoiButton.addActionListener(e -> {
            dispose();
            System.out.println("Przechodzę do list posiadanych pomieszczeń");
            new PosiadanePomieszczeniaGui(idUzytkownika, connection);
        });
        zmianaDanychButton.addActionListener(e -> {
            dispose();
            new ZmianaDanychGui(idUzytkownika, connection);
        });
    }
}
