package Gui;

import Gui.AdminGui.PanelAdministratora;
import Gui.UserGui.PanelUzytkownika;
import dao.DatabaseConnection;
import dao.WeryfikacjaDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class logowanie extends JFrame {
    private JPanel zarzadzanieUzytkownikiem;
    private JPasswordField PassTextField;
    private JTextField LoginTextField;
    private JLabel LoginJLabel;
    private JLabel PassJLabel;
    private JButton zalogujButton;
    private JButton rejestracjaButton;
    private JPanel SystemLogowania;
    String login, password;


public logowanie() {
    super("Logowanie");
    this.setContentPane(this.zarzadzanieUzytkownikiem);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    int width = 400, height = 300;
    this.setSize(width, height);
    this.setVisible(true);
    this.setLocationRelativeTo(null);


    zalogujButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                        new PanelUzytkownika();
                    } else {
                        JOptionPane.showMessageDialog(null, "Błędny login lub hasło!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Wystąpił błąd podczas logowania:" + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Czyszczenie wrażliwych danych
                password = null;
                PassTextField.setText("");

            }
        }
    });
}
}
