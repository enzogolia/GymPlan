package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SerieSvoltaDAO {
    public void doSave(SerieSvolta s) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO SerieSvolta (idSessione, idEsercizio, ripetizioniEseguite, esito, pesoUsato) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, s.getIdSessione());
            ps.setInt(2, s.getIdEsercizio());
            ps.setInt(3, s.getRipetizioniEseguite());
            ps.setInt(4, s.getEsito());
            ps.setDouble(5, s.getPesoUsato());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
