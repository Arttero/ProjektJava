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
    public void addUser(String login, String password) throws SQLException {

        String sql = "INSERT INTO dane_logowania (login,password, rola) VALUES (?,?,'user')";//user to wartosc domyslna
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {//zapytanie sql'owe
            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("Rejestracja udana!");
        }
    }
}
