package dao.AdministratorDAO;

import dao.DatabaseConnection;
import resources.Osoba;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LokatorDAO {
    //szukanie lokatora dla danego pokoju
    public Osoba znajdzLokatoraDlaPokoju(int idPokoju) throws SQLException {
        String sql = "SELECT o.id_lokatora, d.id_osoby, d.imie, d.nazwisko, d.pesel, o.najblizsza_zaplata, o.ostatnia_zaplata, o.id_pokoju " +
                "FROM lokatorzy o JOIN dane_osobowe d ON o.id_osoby = d.id_osoby WHERE o.id_pokoju = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPokoju);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Osoba(
                        rs.getInt("id_osoby"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("pesel"),
                        rs.getDate("najblizsza_zaplata"),
                        rs.getDate("ostatnia_zaplata"),
                        rs.getInt("id_pokoju")
                );
            }
        }
        return null;
    }

    //usuwanie lokatora
    public void usunLokatora(int idOsoby) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM osoby WHERE id_osoby = ?");
                 PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM dane_osobowe WHERE id_osoby = ?")) {

                stmt1.setInt(1, idOsoby);
                stmt1.executeUpdate();

                stmt2.setInt(1, idOsoby);
                stmt2.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(null, "Błąd w bazie danych: "+ e.getMessage(),"Błąd", JOptionPane.WARNING_MESSAGE);
                throw e;
            }
        }
    }

    //dodawanie lokatora
    public void dodajLokatora(String pesel, java.time.LocalDate najblizszaZaplata, java.time.LocalDate ostatniaZaplata, int idPokoju) throws SQLException {
        String sql = "SELECT id_osoby FROM dane_osobowe WHERE pesel = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, pesel);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int idOsoby = rs.getInt("id_osoby");

                    PreparedStatement stmt2 = conn.prepareStatement(
                            "INSERT INTO lokatorzy (id_osoby, najblizsza_zaplata, ostatnia_zaplata, id_pokoju) VALUES (?, ?, ?, ?)"
                    );
                    stmt2.setInt(1, idOsoby);
                    stmt2.setDate(2, Date.valueOf(najblizszaZaplata));
                    stmt2.setDate(3, Date.valueOf(ostatniaZaplata));
                    stmt2.setInt(4, idPokoju);
                    stmt2.executeUpdate();
                    conn.commit();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Wystąpił błąd:" + e.getMessage(),"Błąd", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //edytowanie lokatora
    public void EdytujLokatora(Osoba osoba) throws SQLException {
        String aktualizacjaDaneOsobowe = "UPDATE dane_osobowe SET pesel = ? WHERE id_osoby = ?";
        String aktualizacjaOsoby = "UPDATE lokatorzy SET najblizsza_zaplata = ?, ostatnia_zaplata = ? WHERE id_osoby = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(aktualizacjaDaneOsobowe);
                 PreparedStatement stmt2 = conn.prepareStatement(aktualizacjaOsoby)) {

                // dane_osobowe
                stmt1.setString(1, osoba.getPesel());
                stmt1.setInt(2, osoba.getId());
                stmt1.executeUpdate();

                // osoby
                stmt2.setDate(1, new java.sql.Date(osoba.getNajblizszaZaplata().getTime()));
                stmt2.setDate(2, new java.sql.Date(osoba.getOstatniaZaplata().getTime()));
                stmt2.setInt(3, osoba.getId());
                stmt2.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    //lista lokatorów
    public List<Object[]> pobierzLokatorowZBazy() throws SQLException {
        String sql = "SELECT o.id_lokatora, d.id_osoby, d.imie, d.nazwisko, d.pesel " +
                "FROM lokatorzy o JOIN dane_osobowe d ON o.id_osoby = d.id_osoby";
        List<Object[]> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("pesel")
                });
            }
        }
        return lista;
    }

}


