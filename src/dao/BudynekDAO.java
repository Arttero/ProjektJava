package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudynekDAO {
    public List<Object[]> pobierzWszystkieBudynki() throws SQLException {
        String sql = "SELECT id_budynku, nazwa_budynku, adres, typ_budynku FROM budynki";
        List<Object[]> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_budynku"),
                        rs.getString("nazwa_budynku"),
                        rs.getString("adres"),
                        rs.getString("typ_budynku"),
                });
            }
        }
        return lista;
    }

    public void dodajBudynek(String nazwa, String adres, String typ) throws SQLException {
        String sql = "INSERT INTO budynki (nazwa_budynku, adres, typ_budynku) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nazwa);
            stmt.setString(2, adres);
            stmt.setString(3, typ);
            stmt.executeUpdate();
        }
    }

    public void aktualizujBudynek(int id, String nazwa, String adres, String typ) throws SQLException {
        String sql = "UPDATE budynki SET nazwa_budynku = ?, adres = ?, typ_budynku = ? WHERE id_budynku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nazwa);
            stmt.setString(2, adres);
            stmt.setString(3, typ);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void usunBudynek(int id) throws SQLException {
        String sql = "DELETE FROM budynki WHERE id_budynku = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
