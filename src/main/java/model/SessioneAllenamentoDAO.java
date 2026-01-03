package model;

import java.sql.*;

public class SessioneAllenamentoDAO {
    public int doSave(SessioneAllenamento s) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO SessioneAllenamento (idScheda, data, durata, stato) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getIdScheda());
            ps.setDate(2, new Date(s.getData().getTime()));
            ps.setString(3, s.getDurata());
            ps.setString(4, s.getStato());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int countByScheda(int idScheda) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM SessioneAllenamento WHERE idScheda = ?")) {
            ps.setInt(1, idScheda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
