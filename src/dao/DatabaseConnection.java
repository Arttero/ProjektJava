package dao;

import dao.UzytkownikDAO.WeryfikacjaDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//połączenie z sqlem przy użyciu JDBC
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/system_zarzadzania_budynkiem_db";//nazwa_bazy
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //sprawdzenie loginu i hasła
    public static boolean checkLogin(String login, String password) {
        try {
            WeryfikacjaDAO weryfikacjaDAO = new WeryfikacjaDAO();
            return weryfikacjaDAO.autoryzacjaUzytkownika(login, password);
        } catch (SQLException e) {
            System.err.println("Błąd podczas logowania: " + e.getMessage());
            return false;
        }
    }
}
