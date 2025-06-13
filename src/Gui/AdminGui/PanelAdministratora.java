package Gui.AdminGui;

import Gui.AdminGui.Dodatkowe.BudynekGui;
import Gui.AdminGui.Dodatkowe.WystawRachunekGui;
import Gui.AdminGui.Dodatkowe.PomieszczeniaGui;
import Gui.AdminGui.Dodatkowe.ListaLokatorowGui;
import Gui.JTextFieldZPodpowiedzia;
import Gui.TworzeniePrzyciskowGui;
import Gui.logowanie;
import dao.AdministratorDAO.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class PanelAdministratora extends JFrame {
    private JPanel PanelAdministratora;
    private JButton budynekButton;
    private JButton pokojButton;
    private JButton wylogujButton;
    private JButton ListaLokatorowButton;
    private JButton oplatyButton;



    TworzeniePrzyciskowGui tworzenieGUI = new TworzeniePrzyciskowGui();
    AdminDAO adminDAO = new AdminDAO();

    public PanelAdministratora() {
        super("Panel Administratora");
        PanelAdministratora = new JPanel(new BorderLayout(10,10));
        PanelAdministratora.setBackground(Color.LIGHT_GRAY);
        PanelAdministratora.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panelZNapisem();
        panelZDodawaniemUzytkownika();
        panelDolny();
        ustawienieListenerow();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(PanelAdministratora);
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

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-building3-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);

        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("PANEL ADMINISTRATORA");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 50));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        panelNapis.add(zdjecieLabel);
        panelNapis.add(tekstLabel);


        PanelAdministratora.add(panelNapis,BorderLayout.NORTH);
    }

    private void panelZDodawaniemUzytkownika() {
        //panel służący do dodawania użytkownika
        JPanel panel = new JPanel(new GridLayout(0,2,10,10));
        JTextField loginField = new JTextFieldZPodpowiedzia("Login");
        JTextField imieField = new JTextFieldZPodpowiedzia("Imię");
        JTextField nazwiskoField = new JTextFieldZPodpowiedzia("Nazwisko");
        JTextField peselField = new JTextFieldZPodpowiedzia("PESEL");
        JTextField dataUrodzeniaField = new JTextFieldZPodpowiedzia("YYYY-MM-DD");
        JTextField telefonField = new JTextFieldZPodpowiedzia("Numer telefonu");
        JTextField emailField = new JTextFieldZPodpowiedzia("Adres email");

        JButton dodajButton = tworzenieGUI.tworzeniePrzycisku("Dodaj użytkownika", new Color(2, 2, 2), new Color(255, 255, 255));

        panel.add(new JLabel("Login:"));
        panel.add(loginField);

        panel.add(new JLabel("Imię:"));
        panel.add(imieField);

        panel.add(new JLabel("Nazwisko:"));
        panel.add(nazwiskoField);

        panel.add(new JLabel("PESEL:"));
        panel.add(peselField);

        panel.add(new JLabel("Data urodzenia:"));
        panel.add(dataUrodzeniaField);

        panel.add(new JLabel("Telefon:"));
        panel.add(telefonField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel(""));
        panel.add(dodajButton);

        dodajButton.addActionListener(e -> {
            if(loginField.getText().isEmpty() || imieField.getText().isEmpty() || nazwiskoField.getText().isEmpty() || peselField.getText().isEmpty() || dataUrodzeniaField.getText().isEmpty()
                    || telefonField.getText().isEmpty() || emailField.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"Nie podano wszystkich danych","Błąd",JOptionPane.WARNING_MESSAGE);
            } else {

                try {
                    String login = loginField.getText();
                    String imie = imieField.getText();
                    String nazwisko = nazwiskoField.getText();
                    String pesel = peselField.getText();
                    String dataUrodzenia = dataUrodzeniaField.getText();
                    String telefon = telefonField.getText();
                    String email = emailField.getText();

                    adminDAO.dodanieUzytkownika(login, imie, nazwisko, pesel, dataUrodzenia, telefon, email);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd przy dodawaniu użytkownika:" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        PanelAdministratora.add(panel,BorderLayout.CENTER);
    }


    private void panelDolny(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        PanelAdministratora.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        budynekButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Budynki", new Color(37, 37, 37),new Color(255,255,255), "/resources/icons/icons8-building2-100.png");
        pokojButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Pomieszczenia", new Color(2, 80, 253),new Color(255,255,255), "/resources/icons/icons8-room-100.png");
        ListaLokatorowButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Lista lokatorów", new Color(223, 152, 11),new Color(255,255,255), "/resources/icons/icons8-note-100.png");
        oplatyButton = tworzenieGUI.tworzeniePrzyciskuZeZdjeciem("Opłaty lokatorów", new Color(253, 236, 7),new Color(255,255,255), "/resources/icons/icons8-money-100.png");
        wylogujButton = tworzenieGUI.tworzeniePrzycisku("Wyloguj", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(budynekButton);
        buttonPanel.add(pokojButton);
        buttonPanel.add(ListaLokatorowButton);
        buttonPanel.add(oplatyButton);
        buttonPanel.add(wylogujButton);
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
        ListaLokatorowButton.addActionListener(e -> {
            dispose();
            System.out.println("Przechodzę do listy lokatorów");
            new ListaLokatorowGui();
        });
        oplatyButton.addActionListener( e -> {
            dispose();
            System.out.println("Przechodzę do gui opłat");
            new WystawRachunekGui();
        });
    }
}