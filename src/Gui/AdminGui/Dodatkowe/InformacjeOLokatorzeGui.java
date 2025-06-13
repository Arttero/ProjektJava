package Gui.AdminGui.Dodatkowe;

import dao.AdministratorDAO.LokatorDAO;
import resources.Osoba;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class InformacjeOLokatorzeGui extends JFrame {
    public void zarzadzajLokatorem(int idPokoju) {
        //możliwość sprawdzenia/edytowania/dodania lokatora
        try {
            LokatorDAO lokatorDAO = new LokatorDAO();
            Osoba lokator = lokatorDAO.znajdzLokatoraDlaPokoju(idPokoju);

            if (lokator == null) {
                int opcja = JOptionPane.showConfirmDialog(this, "Brak lokatora w tym pokoju. Czy chcesz dodać nowego?", "Brak lokatora", JOptionPane.YES_NO_OPTION);
                if (opcja == JOptionPane.YES_OPTION) {
                    DodajLokatora(idPokoju);
                }
            } else {
                String[] opcje = {"Edytuj", "Usuń", "Anuluj"};
                int wybor = JOptionPane.showOptionDialog(this,
                        "Lokator: " + lokator.getImie() + " " + lokator.getNazwisko(),
                        "Zarządzanie lokatorem",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcje, opcje[0]);

                if (wybor == 0) {
                    EdytujLokatora(lokator);
                } else if (wybor == 1) {
                    int potwierdzenie = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć lokatora?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
                    if (potwierdzenie == JOptionPane.YES_OPTION) {
                        lokatorDAO.usunLokatora(lokator.getId());
                        JOptionPane.showMessageDialog(this, "Lokator został usunięty.");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    //dodanie lokatora
    private void DodajLokatora(int idPokoju) {
        JTextField peselField = new JTextField();
        JTextField ostatniaZaplataField = new JTextField();
        JTextField najblizszaZaplataField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("PESEL:"));
        panel.add(peselField);
        panel.add(new JLabel("Ostatnia zapłata (RRRR-MM-DD):"));
        panel.add(ostatniaZaplataField);
        panel.add(new JLabel("Najbliższa zapłata (RRRR-MM-DD):"));
        panel.add(najblizszaZaplataField);

        int wynik = JOptionPane.showConfirmDialog(this, panel, "Dodaj lokatora", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                String pesel = peselField.getText().trim();
                LocalDate ostatniaZaplata = LocalDate.parse(ostatniaZaplataField.getText().trim());
                LocalDate najblizszaZaplata = LocalDate.parse(najblizszaZaplataField.getText().trim());

                LokatorDAO dao = new LokatorDAO();
                dao.dodajLokatora(pesel, najblizszaZaplata, ostatniaZaplata, idPokoju);
                JOptionPane.showMessageDialog(this, "Dodano lokatora.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty (użyj RRRR-MM-DD).", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //edycja lokatora
    private void EdytujLokatora(Osoba lokator) {
        JTextField peselField = new JTextField(lokator.getPesel());
        JTextField ostatniaZaplataField = new JTextField(lokator.getOstatniaZaplata().toString());
        JTextField najblizszaZaplataField = new JTextField(lokator.getNajblizszaZaplata().toString());

        //odczytanie daty z Date na LocalDate
        LocalDate ostatniaZaplataLocal = LocalDate.parse(ostatniaZaplataField.getText().trim());
        LocalDate najblizszaZaplataLocal = LocalDate.parse(najblizszaZaplataField.getText().trim());

        Date ostatniaZaplata = Date.from(ostatniaZaplataLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date najblizszaZaplata = Date.from(najblizszaZaplataLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());


        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("PESEL:"));
        panel.add(peselField);
        panel.add(new JLabel("Ostatnia zapłata (RRRR-MM-DD):"));
        panel.add(ostatniaZaplataField);
        panel.add(new JLabel("Najbliższa zapłata (RRRR-MM-DD):"));
        panel.add(najblizszaZaplataField);

        int wynik = JOptionPane.showConfirmDialog(this, panel, "Edytuj lokatora", JOptionPane.OK_CANCEL_OPTION);
        if (wynik == JOptionPane.OK_OPTION) {
            try {
                lokator.setPesel(peselField.getText().trim());
                lokator.setOstatniaZaplata(ostatniaZaplata);
                lokator.setNajblizszaZaplata(najblizszaZaplata);

                LokatorDAO dao = new LokatorDAO();
                dao.EdytujLokatora(lokator);
                JOptionPane.showMessageDialog(this, "Zaktualizowano dane lokatora.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty (użyj RRRR-MM-DD).", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
