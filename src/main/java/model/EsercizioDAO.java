package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EsercizioDAO {

    public List<Esercizio> doRetrieveAll() {
        List<Esercizio> esercizi = new ArrayList<>();
        // Assicurati che la query selezioni tutto (*) o elenca le colonne inclusa 'immagine'
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Esercizio");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Esercizio e = mapRowToEntity(rs); // Uso un metodo helper per pulizia
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
                    return mapRowToEntity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Metodo di utilità per evitare di riscrivere il codice di mapping due volte
    private Esercizio mapRowToEntity(ResultSet rs) throws SQLException {
        Esercizio e = new Esercizio();
        e.setIdEsercizio(rs.getInt("IdEsercizio"));
        e.setNome(rs.getString("nome"));
        e.setGruppoMuscolare(rs.getString("gruppoMuscolare"));
        e.setSerieSuggerite(rs.getInt("serieSuggerite"));
        e.setRipetizioni(rs.getInt("ripetizioni"));
        e.setCaricoSuggerito(rs.getInt("caricoSuggerito"));
        e.setRecupero(rs.getTime("recupero"));

        String liv = rs.getString("livello");
        e.setLivello(liv != null ? liv : "principiante");

        // --- LETTURA NUOVA COLONNA ---
        e.setImmagine(rs.getString("image"));

        return e;
    }
}