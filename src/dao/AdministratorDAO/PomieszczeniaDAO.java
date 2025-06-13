package dao.AdministratorDAO;

import dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PomieszczeniaDAO {

    //pobieranie wszystkich pokoi
    public List<Object[]> pobierzWszystkiePomieszczenia() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.id_pokoju, b.id_budynku, b.nazwa_budynku, b.adres, p.typ_pomieszczenia, p.czy_zajete, p.cena_czynszu, p.cena_zakupu " +
                "FROM pokoje p JOIN budynki b ON p.id_budynku = b.id_budynku";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_pokoju"),
                        rs.getInt("id_budynku"),
                        rs.getString("typ_pomieszczenia"),
                        "Tak".equalsIgnoreCase(rs.getString("czy_zajete")) ? "Tak" : "Nie",
                        rs.getInt("cena_czynszu"),
                        rs.getObject("cena_zakupu")
                });
            }
        }

        return lista;
    }

    //dodawanie nowego pokoju
    public void dodajPomieszczenie(int idBudynek, String typ, String czyZajete, int cenaCzynszu, Integer cenaZakupu) throws SQLException {
        String sql = "INSERT INTO pokoje (id_budynku, typ_pomieszczenia, czy_zajete, cena_czynszu, cena_zakupu) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBudynek);
            stmt.setString(2, typ);
            stmt.setString(3, czyZajete);
            stmt.setInt(4, cenaCzynszu);
            if (cenaZakupu == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, cenaZakupu);
            }

            stmt.executeUpdate();
        }
    }

    //aktualizacja pokoju
    public void aktualizujPomieszczenie(int idPokoju, int idBudynek, String typ, String czyZajete, int cenaCzynszu, int cenaZakupu) throws SQLException {
        String sql = "UPDATE pokoje SET id_budynku = ?, typ_pomieszczenia = ?, czy_zajete = ?, cena_czynszu = ?, cena_zakupu = ? WHERE id_pokoju = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBudynek);
            stmt.setString(2, typ);
            stmt.setString(3, czyZajete);
            stmt.setInt(4, cenaCzynszu);
            stmt.setInt(5, cenaZakupu);
            stmt.setInt(6, idPokoju);

            stmt.executeUpdate();
        }
    }

    //usuwanie pokoju
    public void usunPomieszczenie(int idPokoju) throws SQLException {
        String sql = "DELETE FROM pokoje WHERE id_pokoju = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPokoju);
            stmt.executeUpdate();
        }
    }
}
