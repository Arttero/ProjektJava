package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/uzytkownicy";//nazwa_bazy
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static boolean checkLogin(String login, String password) {
        try {
            UserDAO userDAO = new UserDAO();
            return userDAO.authenticateUser(login, password);
        } catch (SQLException e) {
            System.err.println("Błąd podczas logowania: " + e.getMessage());
            return false;
        }
    }
}
