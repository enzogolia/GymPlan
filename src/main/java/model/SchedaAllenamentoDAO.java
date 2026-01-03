package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchedaAllenamentoDAO {
            // Recupera una scheda per idScheda
            public SchedaAllenamento doRetrieveById(int idScheda) {
                try (Connection con = ConPool.getConnection();
                     PreparedStatement ps = con.prepareStatement("SELECT * FROM SchedaAllenamento WHERE IdScheda = ?")) {
                    ps.setInt(1, idScheda);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            SchedaAllenamento s = new SchedaAllenamento();
                            s.setIdScheda(rs.getInt("IdScheda"));
                            s.setIdUser(rs.getInt("IdUser"));
                            s.setNome(rs.getString("nome"));
                            s.setTipo(rs.getString("tipo"));
                            s.setGiorni(rs.getInt("giorni"));
                            s.setFocus(rs.getString("focus"));
                            s.setLivello(rs.getInt("livello"));
                            s.setIntensita(rs.getInt("intensità"));
                            s.setNote(rs.getString("note"));
                            return s;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        // Recupera l'id della scheda tramite nome, tipo e idUser
        public int doRetrieveIdByNomeTipoUser(String nome, String tipo, int idUser) {
            try (Connection con = ConPool.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                    "SELECT IdScheda FROM SchedaAllenamento WHERE nome = ? AND tipo = ? AND IdUser = ? ORDER BY IdScheda DESC LIMIT 1")) {
                ps.setString(1, nome);
                ps.setString(2, tipo);
                ps.setInt(3, idUser);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return -1;
        }
    public List<SchedaAllenamento> doRetrieveAll() {
        List<SchedaAllenamento> schede = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM SchedaAllenamento");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SchedaAllenamento s = new SchedaAllenamento();
                s.setIdScheda(rs.getInt("IdScheda"));
                s.setIdUser(rs.getInt("IdUser"));
                s.setNome(rs.getString("nome"));
                s.setTipo(rs.getString("tipo"));
                s.setGiorni(rs.getInt("giorni"));
                s.setFocus(rs.getString("focus"));
                s.setLivello(rs.getInt("livello"));
                s.setIntensita(rs.getInt("intensità"));
                s.setNote(rs.getString("note"));
                schede.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schede;
    }

    // Recupera tutte le schede associate a uno specifico utente
    public List<SchedaAllenamento> doRetrieveByUserId(int idUser) {
        List<SchedaAllenamento> schede = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM SchedaAllenamento WHERE IdUser = ?")) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SchedaAllenamento s = new SchedaAllenamento();
                    s.setIdScheda(rs.getInt("IdScheda"));
                    s.setIdUser(rs.getInt("IdUser"));
                    s.setNome(rs.getString("nome"));
                    s.setTipo(rs.getString("tipo"));
                    s.setGiorni(rs.getInt("giorni"));
                    s.setFocus(rs.getString("focus"));
                    s.setLivello(rs.getInt("livello"));
                    s.setIntensita(rs.getInt("intensità"));
                    s.setNote(rs.getString("note"));
                    schede.add(s);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schede;
    }
    // Salva una nuova scheda
    public void doSave(SchedaAllenamento scheda) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO SchedaAllenamento (IdUser, nome, tipo, giorni, focus, livello, intensità, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, scheda.getIdUser());
            ps.setString(2, scheda.getNome());
            ps.setString(3, scheda.getTipo());
            ps.setInt(4, scheda.getGiorni());
            ps.setString(5, scheda.getFocus());
            ps.setInt(6, scheda.getLivello());
            ps.setInt(7, scheda.getIntensita());
            ps.setString(8, scheda.getNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}