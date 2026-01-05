package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EsercizioDAO {
    public List<Esercizio> doRetrieveAll() {
        List<Esercizio> esercizi = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Esercizio");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Esercizio e = new Esercizio();
                e.setIdEsercizio(rs.getInt("IdEsercizio"));
                e.setNome(rs.getString("nome"));
                e.setGruppoMuscolare(rs.getString("gruppoMuscolare"));
                e.setSerieSuggerite(rs.getInt("serieSuggerite"));
                e.setRipetizioni(rs.getInt("ripetizioni"));
                e.setCaricoSuggerito(rs.getInt("caricoSuggerito"));
                e.setRecupero(rs.getTime("recupero"));

                // MODIFICATO: Lettura Stringa
                String liv = rs.getString("livello");
                e.setLivello(liv != null ? liv : "Principiante"); // Default safe

                esercizi.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return esercizi;
    }

    public Esercizio doRetrieveById(int id) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Esercizio WHERE IdEsercizio = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Esercizio e = new Esercizio();
                    e.setIdEsercizio(rs.getInt("IdEsercizio"));
                    e.setNome(rs.getString("nome"));
                    e.setGruppoMuscolare(rs.getString("gruppoMuscolare"));
                    e.setSerieSuggerite(rs.getInt("serieSuggerite"));
                    e.setRipetizioni(rs.getInt("ripetizioni"));
                    e.setCaricoSuggerito(rs.getInt("caricoSuggerito"));
                    e.setRecupero(rs.getTime("recupero"));

                    // MODIFICATO: Lettura Stringa
                    String liv = rs.getString("livello");
                    e.setLivello(liv != null ? liv : "Principiante");

                    return e;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}