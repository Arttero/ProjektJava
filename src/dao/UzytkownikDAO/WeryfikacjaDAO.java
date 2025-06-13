package dao.UzytkownikDAO;

import dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeryfikacjaDAO {
    //autoryzacja użytkownika
    public boolean autoryzacjaUzytkownika(String login, String password) throws SQLException {
        String sql = "SELECT * FROM dane_logowania WHERE login=? AND password=?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {//zapytanie sql'owe
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    //pobieranie roli użytkownika
    public String pobierzRole(String login, String password) throws SQLException {
        String sql = "SELECT rola FROM dane_logowania WHERE login=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("rola");
            }
        }
        return null; // nie znaleziono użytkownika
    }

    //pobieranie ID użytkownika (potrzebne do całego Gui użytkownika)
    public int pobierzIdUzytkownika(String login) throws SQLException {
        String sql = "SELECT id_logowania FROM dane_logowania WHERE login=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_logowania");
            }
        }
        return -1; // nie znaleziono użytkownika
    }

}
