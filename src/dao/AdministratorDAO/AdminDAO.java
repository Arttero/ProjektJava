package dao.AdministratorDAO;

import dao.DatabaseConnection;

import javax.swing.*;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class AdminDAO {
    //generowanie losowego hasła
    public String generowanieLosowegoHasla() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[8]; //mniej więcej 11 znaków
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    //dodawanie użytkownika do bazy danych
    public void dodanieUzytkownika(String login, String imie, String nazwisko, String pesel, String data_urodzenia, String telefon, String email) throws SQLException {

        String password = generowanieLosowegoHasla();
        Connection conn = null;
        PreparedStatement stmtLogin = null;
        PreparedStatement stmtPersonal = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlLogin = "INSERT INTO dane_logowania (login,password, rola) VALUES (?,?,'user')";//user to wartość domyślna
            stmtLogin = conn.prepareStatement(sqlLogin, Statement.RETURN_GENERATED_KEYS);
            stmtLogin.setString(1, login);
            stmtLogin.setString(2, password);
            stmtLogin.executeUpdate();

            ResultSet rs = stmtLogin.getGeneratedKeys();
            int id_logowania = -1;
            if (rs.next()) {
                id_logowania = rs.getInt(1);
            }
            String sqlPersonal = "INSERT INTO dane_osobowe (imie, nazwisko, pesel,  data_urodzenia, telefon, email, id_logowania) VALUES (?,?,?,?,?,?,?)";


            stmtPersonal = conn.prepareStatement(sqlPersonal);
            stmtPersonal.setString(1, imie);
            stmtPersonal.setString(2, nazwisko);
            stmtPersonal.setString(3, pesel);
            stmtPersonal.setString(4, data_urodzenia);
            stmtPersonal.setString(5, telefon);
            stmtPersonal.setString(6, email);
            stmtPersonal.setString(7, String.valueOf(id_logowania));
            stmtPersonal.executeUpdate();

            conn.commit();

            String komunikat = "Login: " + login + "\nHasło: " + password;
            JTextArea textArea = new JTextArea(komunikat);
            textArea.setEditable(false);
            textArea.setBackground(null);
            textArea.setBorder(null);

            JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Dane dostępowe", JOptionPane.INFORMATION_MESSAGE);

        }catch (Exception ex) {
            System.out.println("Wystąpił błąd podczas rejestrowania: " + ex.getMessage());
        }
    }
}
