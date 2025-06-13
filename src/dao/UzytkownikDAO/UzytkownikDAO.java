package dao.UzytkownikDAO;

import resources.Uzytkownik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UzytkownikDAO{
    private Connection conn;

    public UzytkownikDAO(Connection conn) {
        this.conn = conn;
    }

    //aktualizacja danych użytkownika
    public boolean aktualizujDaneUzytkownika(int idLogowania, String login, String haslo, String telefon, String email) {
        try {
            conn.setAutoCommit(false);

            //aktualizacja logowania
            String sql = "UPDATE dane_logowania SET login = ?, password = ? WHERE id_logowania = ?";
            try (PreparedStatement stmt1 = conn.prepareStatement(sql)) {
                stmt1.setString(1, login);
                stmt1.setString(2, haslo);
                stmt1.setInt(3, idLogowania);
                stmt1.executeUpdate();
            }

            //aktualizacja telefonu i maila
            String sql2 = "UPDATE dane_osobowe SET telefon = ?, email = ? WHERE id_logowania = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setString(1, telefon);
                stmt2.setString(2, email);
                stmt2.setInt(3, idLogowania);
                stmt2.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback(); //próba cofnięcia zmian
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    //pobieranie danych użytkownika
    public Uzytkownik pobierzUzytkownika(int idLogowania) {
        String sql = "SELECT login, password, telefon, email FROM dane_logowania " +
                "JOIN dane_osobowe ON dane_logowania.id_logowania = dane_osobowe.id_logowania " +
                "WHERE dane_logowania.id_logowania = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLogowania);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Uzytkownik(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("telefon"),
                        rs.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}