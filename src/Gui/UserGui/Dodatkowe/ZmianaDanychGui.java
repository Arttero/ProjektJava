package Gui.UserGui.Dodatkowe;

import Gui.JTextFieldZPodpowiedzia;
import Gui.TworzeniePrzyciskowGui;
import Gui.UserGui.PanelUzytkownika;
import dao.UzytkownikDAO.UzytkownikDAO;
import resources.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ZmianaDanychGui extends JFrame {
    private JPanel panelGlowny;
    private JTextField poleLogin;
    private JTextField poleHaslo;
    private JTextField poleEmail;
    private JTextField poleTelefon;
    private JButton aktualizujDaneButton;
    private JButton powrotButton;
    private int idUzytkownika;
    private Connection connection;


    private TworzeniePrzyciskowGui tworzenieGUI = new TworzeniePrzyciskowGui();

    public ZmianaDanychGui(int idUzytkownika, Connection connection) {
        super("Zmiana danych");
        this.idUzytkownika = idUzytkownika;
        this.connection = connection;
        panelGlowny = new JPanel(new BorderLayout(10,10));
        panelGlowny.setBackground(Color.LIGHT_GRAY);
        panelGlowny.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panelGorny();
        panelSrodkowy();
        panelDolny();
        ustawienieListenerow();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        this.setContentPane(panelGlowny);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void panelGorny() {
        JPanel panelNapis = new JPanel();
        panelNapis.setLayout(new BoxLayout(panelNapis,BoxLayout.X_AXIS));
        panelNapis.setBackground(Color.WHITE);
        panelNapis.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/icons/icons8-user-100.png"));
        Image zeskalowaneZdjecie = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon zeskalowanaIkona = new ImageIcon(zeskalowaneZdjecie);

        JLabel zdjecieLabel = new JLabel(zeskalowanaIkona);
        zdjecieLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel tekstLabel = new JLabel("ZMIANA DANYCH");
        tekstLabel.setFont(new Font("Arial", Font.BOLD, 50));
        tekstLabel.setForeground(Color.BLACK);
        tekstLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        tekstLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        panelNapis.add(zdjecieLabel);
        panelNapis.add(tekstLabel);

        panelGlowny.add(panelNapis,BorderLayout.NORTH);
    }

    public void panelSrodkowy() {
        JPanel panelSrodkowy = new JPanel(new GridLayout(4, 2, 15, 10));
        panelSrodkowy.setBackground(Color.WHITE);
        panelSrodkowy.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panelSrodkowy.add(new JLabel("Nowy login:"));
        poleLogin = new JTextFieldZPodpowiedzia("Wprowadź nowy login");
        panelSrodkowy.add(poleLogin);

        panelSrodkowy.add(new JLabel("Nowe hasło:"));
        poleHaslo = new JTextFieldZPodpowiedzia("Wprowadź nowe hasło");
        panelSrodkowy.add(poleHaslo);

        panelSrodkowy.add(new JLabel("Nowy email:"));
        poleEmail = new JTextFieldZPodpowiedzia("Wprowadź nowy email");
        panelSrodkowy.add(poleEmail);

        panelSrodkowy.add(new JLabel("Nowy telefon:"));
        poleTelefon = new JTextFieldZPodpowiedzia("Wprowadź nowy telefon");
        panelSrodkowy.add(poleTelefon);


        panelGlowny.add(panelSrodkowy, BorderLayout.CENTER);
    }

    public void panelDolny() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        panelGlowny.add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
        aktualizujDaneButton = tworzenieGUI.tworzeniePrzycisku("Aktualizuj dane", new Color(25, 195, 31), new Color(255,255,255));
        powrotButton = tworzenieGUI.tworzeniePrzycisku("Powrót", new Color(211, 38, 38), new Color(255,255,255));

        buttonPanel.add(aktualizujDaneButton);
        buttonPanel.add(powrotButton);
    }
    public void ustawienieListenerow() {
        aktualizujDaneButton.addActionListener(e -> {
            UzytkownikDAO dao = new UzytkownikDAO(connection);
            Uzytkownik obecneDane = dao.pobierzUzytkownika(idUzytkownika);

            if (obecneDane == null) {
                JOptionPane.showMessageDialog(this, "Nie udało się pobrać danych.");
                return;
            }

            String nowyLogin = poleLogin.getText().trim().isEmpty() ? obecneDane.getLogin() : poleLogin.getText().trim();
            String noweHaslo = poleHaslo.getText().trim().isEmpty() ? obecneDane.getHaslo() : poleHaslo.getText().trim();
            String nowyEmail = poleEmail.getText().trim().isEmpty() ? obecneDane.getEmail() : poleEmail.getText().trim();
            String nowyTelefon = poleTelefon.getText().trim().isEmpty() ? obecneDane.getTelefon() : poleTelefon.getText().trim();

            boolean sukces = dao.aktualizujDaneUzytkownika(idUzytkownika, nowyLogin, noweHaslo, nowyTelefon, nowyEmail);
            if (sukces) {
                JOptionPane.showMessageDialog(this, "Dane zostały zaktualizowane.");
            } else {
                JOptionPane.showMessageDialog(this, "Błąd podczas aktualizacji danych.");
            }
        });

        powrotButton.addActionListener(e -> {
            dispose();
            new PanelUzytkownika(idUzytkownika, connection);
        });
    }
}
