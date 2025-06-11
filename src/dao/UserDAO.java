package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean authenticateUser(String login,String password) throws SQLException {
        String sql = "SELECT * FROM dane_logowania WHERE login=? AND password=?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {//zapytanie sql'owe
            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();//zwroci true jezeli bedzie dopasowanie
        }

    }
    public void addUser(String login, String password, String imie,
                        String nazwisko, String data_urodzenia, int telefon, String email) throws SQLException {

        Connection conn = null;
        PreparedStatement stmtLogin = null;
        PreparedStatement stmtPersonal = null;
        ResultSet rs = null;


        String sqlLogin = "INSERT INTO dane_logowania (login,password, rola) VALUES (?,?,'user')";//user to wartosc domyslna
        try {
            conn = DatabaseConnection.getConnection();
            stmtLogin = conn.prepareStatement(sqlLogin);//zapytanie sql'owe
            stmtLogin.setString(1, login);
            stmtLogin.setString(2, password);
            stmtLogin.executeUpdate();
            System.out.println("Rejestracja udana!");
        } catch (Exception ex) {
            System.out.println("Wystąpił błąd podczas rejestrowania: " + ex.getMessage());
        }
        String sqlPersonal = "INSERT INTO dane_osobowe (imie, nazwisko, data_urodzenia, telefon, email) VALUES (?,?,?,?,?)";
        try {
            conn = DatabaseConnection.getConnection();
            stmtPersonal = conn.prepareStatement(sqlPersonal);
            stmtPersonal.setString(1, imie);
            stmtPersonal.setString(2, nazwisko);
            stmtPersonal.setString(3, data_urodzenia);
            stmtPersonal.setString(4, String.valueOf(telefon));
            stmtPersonal.setString(5, email);
            stmtPersonal.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Wystąpił błąd podczas rejestrowania: " + ex.getMessage());
        }
    }
}
