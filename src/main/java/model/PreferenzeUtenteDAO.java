package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreferenzeUtenteDAO {
    public PreferenzeUtente doRetrieveByUserId(int idUser) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM PreferenzeUtente WHERE IdUser = ?")) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PreferenzeUtente p = new PreferenzeUtente();
                p.setIdUser(rs.getInt("IdUser"));
                p.setGiorniDisponibili(rs.getString("giorniDisponibili"));
                p.setGruppiMuscolari(rs.getString("gruppiMuscolari"));
                p.setInfortuni(rs.getString("infortuni"));
                p.setLivelloEsperienza(rs.getString("livelloEsperienza"));
                try {
                    p.setIntensita(rs.getInt("intensità"));
                } catch (SQLException e) {
                    // Colonna non trovata
                }
                return p;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void saveOrUpdate(PreferenzeUtente preferenze) {
        try (Connection con = ConPool.getConnection()) {
            // Update, se non aggiorna allora inserisce
            PreparedStatement ps = con.prepareStatement("UPDATE PreferenzeUtente SET giorniDisponibili=?, gruppiMuscolari=?, infortuni=?, livelloEsperienza=?, `intensità`=? WHERE IdUser=?");
            ps.setString(1, preferenze.getGiorniDisponibili());
            ps.setString(2, preferenze.getGruppiMuscolari());
            ps.setString(3, preferenze.getInfortuni());
            ps.setString(4, preferenze.getLivelloEsperienza());
            ps.setInt(5, preferenze.getIntensita());
            ps.setInt(6, preferenze.getIdUser());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                ps = con.prepareStatement("INSERT INTO PreferenzeUtente (IdUser, giorniDisponibili, gruppiMuscolari, infortuni, livelloEsperienza, `intensità`) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, preferenze.getIdUser());
                ps.setString(2, preferenze.getGiorniDisponibili());
                ps.setString(3, preferenze.getGruppiMuscolari());
                ps.setString(4, preferenze.getInfortuni());
                ps.setString(5, preferenze.getLivelloEsperienza());
                ps.setInt(6, preferenze.getIntensita());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
