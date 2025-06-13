package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RachunkiDAO {
    private Connection connection;

    public RachunkiDAO(Connection connection) {
        this.connection = connection;
    }

    //lista opłat użytkownika
    public List<Object[]> pobierzOplatyDlaUzytkownika(int idOsoby) {
        List<Object[]> oplaty = new ArrayList<>();

        String sql = "SELECT  r.id_rachunku, r.id_pokoju, r.opis, r.kwota, r.termin_platnosci, r.status " +
                "FROM rachunki r JOIN lokatorzy o ON r.id_lokatora = o.id_lokatora WHERE o.id_osoby = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idOsoby);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] wiersz = new Object[6];
                wiersz[0] = rs.getInt("id_rachunku");
                wiersz[1] = rs.getInt("id_pokoju");
                wiersz[2] = rs.getString("opis");
                wiersz[3] = rs.getBigDecimal("kwota").toString();
                wiersz[4] = rs.getDate("termin_platnosci").toString();
                wiersz[5] = rs.getString("status");
                oplaty.add(wiersz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oplaty;
    }

    //opłacanie rachunku
    public void oplacRachunek(int idRachunku) {
        String sql = "UPDATE rachunki SET status = 'OPŁACONY' WHERE id_rachunku = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRachunku);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //wystawienie rachunku
    public boolean wystawRachunek(int idPokoju, String opis, Date terminPlatnosci) {
        try {
            String sqlLokator = "SELECT id_lokatora FROM lokatorzy WHERE id_pokoju = ?";
            PreparedStatement psLokator = connection.prepareStatement(sqlLokator);
            psLokator.setInt(1, idPokoju);
            ResultSet rsLokator = psLokator.executeQuery();

            if (!rsLokator.next()) {
                return false; // brak lokatora
            }

            int idLokatora = rsLokator.getInt("id_lokatora");

            String sqlCena = "SELECT cena_czynszu FROM pokoje WHERE id_pokoju = ?";
            PreparedStatement psCena = connection.prepareStatement(sqlCena);
            psCena.setInt(1, idPokoju);
            ResultSet rsCena = psCena.executeQuery();

            if (!rsCena.next()) {
                return false;
            }

            double kwota = rsCena.getDouble("cena_czynszu");

            String sql = "INSERT INTO rachunki (id_lokatora, id_pokoju, opis, kwota, termin_platnosci, status) VALUES (?, ?, ?, ?, ?, 'NIEOPŁACONY')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idLokatora);
            stmt.setInt(2, idPokoju);
            stmt.setString(3, opis);
            stmt.setDouble(4, kwota);
            stmt.setDate(5, new java.sql.Date(terminPlatnosci.getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

