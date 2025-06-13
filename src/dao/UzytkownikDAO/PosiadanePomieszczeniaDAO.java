package dao.UzytkownikDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PosiadanePomieszczeniaDAO {
    //posiadane pomieszczenia przez daną osobę
    public List<Object[]> pobierzPosiadanePomieszczeniaUzytkownika(int idLogowania, Connection connection) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT budynki.id_budynku, budynki.adres, budynki.typ_budynku, pokoje.id_pokoju " +
                "FROM dane_logowania " +
                "JOIN dane_osobowe ON dane_logowania.id_logowania = dane_osobowe.id_logowania " +
                "JOIN lokatorzy ON dane_osobowe.id_osoby = lokatorzy.id_osoby " +
                "JOIN pokoje ON lokatorzy.id_pokoju = pokoje.id_pokoju " +
                "JOIN budynki ON pokoje.id_budynku = budynki.id_budynku " +
                "WHERE dane_logowania.id_logowania = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idLogowania);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_budynku"),
                        rs.getString("adres"),
                        rs.getString("typ_budynku"),
                        rs.getInt("id_pokoju")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
