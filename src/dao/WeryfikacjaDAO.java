package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeryfikacjaDAO {
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
}
