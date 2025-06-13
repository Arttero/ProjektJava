package Gui.AdminGui.Dodatkowe;

import Gui.AdminGui.PanelAdministratora;
import Gui.TworzeniePrzyciskowGui;
import dao.DatabaseConnection;
import dao.RachunkiDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;

public class WystawRachunekGui extends JFrame {
    private JTextField poleIdPokoju;
    private JTextField poleOpis;
    private JSpinner spinnerTermin;
    private JButton przyciskWystaw;
    private JButton zamknijButton;

    TworzeniePrzyciskowGui tworzeniePrzyciskowGui = new TworzeniePrzyciskowGui();

    public WystawRachunekGui() {
        super("Wystaw Rachunek");

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icons/icons8-home-64.png"));
        setIconImage(icon);

        poleIdPokoju = new JTextField();
        poleOpis = new JTextField();
        spinnerTermin = new JSpinner(new SpinnerDateModel());
        przyciskWystaw = tworzeniePrzyciskowGui.tworzeniePrzycisku( "Wystaw rachunek", new Color(25, 195, 31), new Color(255,255,255));
        zamknijButton = tworzeniePrzyciskowGui.tworzeniePrzycisku( "Powrót", new Color(211, 38, 38), new Color(255,255,255));

        panel.add(new JLabel("ID pokoju:"));
        panel.add(poleIdPokoju);
        panel.add(new JLabel("Opis:"));
        panel.add(poleOpis);
        panel.add(new JLabel("Termin płatności:"));
        panel.add(spinnerTermin);
        panel.add(przyciskWystaw);
        panel.add(zamknijButton);

        add(panel);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        przyciskWystaw.addActionListener(e -> {
            try {
                Connection connection = DatabaseConnection.getConnection();
                int idPokoju = Integer.parseInt(poleIdPokoju.getText());
                String opis = poleOpis.getText();
                java.util.Date utilDate = (java.util.Date) spinnerTermin.getValue();
                java.sql.Date termin = new java.sql.Date(utilDate.getTime());


                RachunkiDAO rachunkiDAO = new RachunkiDAO(connection);
                boolean sukces = rachunkiDAO.wystawRachunek(idPokoju, opis, termin);
                JOptionPane.showMessageDialog(this,
                        sukces ? "Rachunek wystawiony!" : "Błąd podczas wystawiania rachunku.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd danych: " + ex.getMessage());
            }
        });
        zamknijButton.addActionListener(e -> {
            dispose();
            new PanelAdministratora();
        });
    }
}
