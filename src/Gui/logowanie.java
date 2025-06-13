package Gui;

import Gui.AdminGui.PanelAdministratora;
import Gui.UserGui.PanelUzytkownika;
import dao.DatabaseConnection;
import dao.UzytkownikDAO.WeryfikacjaDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class logowanie extends JFrame {
    private JPanel zarzadzanieUzytkownikiem;
    private JPasswordField PassTextField;
    private JTextField LoginTextField;
    private JButton zalogujButton;
    private JButton niePamiętamHaslaButton;
    String login, password;
    private Connection connection;


public logowanie() {
    super("Logowanie");
    this.setContentPane(this.zarzadzanieUzytkownikiem);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
    setIconImage(icon);

    int width = 400, height = 300;
    this.setSize(width, height);
    this.setVisible(true);
    this.setLocationRelativeTo(null);


    zalogujButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connection = DatabaseConnection.getConnection();
                if (connection == null) {
                    JOptionPane.showMessageDialog(null, "Brak połączenia z bazą danych!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (SQLException ex) {
                System.out.println("Błąd:" + ex.getMessage());
            }

            //sprawdzenie czy pole nie jest puste
            if (LoginTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Wprowadź login!", "Błąd", JOptionPane.WARNING_MESSAGE);

            } else if (new String(PassTextField.getPassword()).isEmpty()){
                JOptionPane.showMessageDialog(null,
                        "Wprowadź hasło!", "Błąd", JOptionPane.WARNING_MESSAGE);
            }

            //przypisanie danych pod zmienne
            login = LoginTextField.getText();
            password = new String(PassTextField.getPassword());

            try {
                if (DatabaseConnection.checkLogin(login, password)) {
                    JOptionPane.showMessageDialog(null, "Zalogowano pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    WeryfikacjaDAO weryfikacjaDAO = new WeryfikacjaDAO();
                    String rola = weryfikacjaDAO.pobierzRole(login, password);
                    setVisible(false);
                    if ("admin".equalsIgnoreCase(rola)) {
                        new PanelAdministratora();
                    } else if ("user".equalsIgnoreCase(rola)) {
                        int idUzytkownika = weryfikacjaDAO.pobierzIdUzytkownika(login);
                        new PanelUzytkownika(idUzytkownika, connection);
                    } else {
                        JOptionPane.showMessageDialog(null, "Błędny login lub hasło!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Podano błędne dane","Błąd",JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Wystąpił błąd podczas logowania:" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } finally {
                //czyszczenie wrażliwych danych
                password = null;
                PassTextField.setText("");

            }
        }
    });
    niePamiętamHaslaButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Skontaktuj się z administratorem aby odzyskać hasło.","Informacja",JOptionPane.INFORMATION_MESSAGE);
        }
    });
}
}
