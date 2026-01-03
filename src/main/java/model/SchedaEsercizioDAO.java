package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchedaEsercizioDAO {
        public void doSave(SchedaEsercizio se) {
            try (Connection con = ConPool.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO SchedaEsercizio (idScheda, idEsercizio) VALUES (?, ?)");) {
                ps.setInt(1, se.getIdScheda());
                ps.setInt(2, se.getIdEsercizio());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    public List<SchedaEsercizio> doRetrieveByIdScheda(int idScheda) {
        List<SchedaEsercizio> lista = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM SchedaEsercizio WHERE idScheda = ?")) {
            ps.setInt(1, idScheda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SchedaEsercizio se = new SchedaEsercizio();
                    se.setIdScheda(rs.getInt("idScheda"));
                    se.setIdEsercizio(rs.getInt("idEsercizio"));
                    lista.add(se);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
