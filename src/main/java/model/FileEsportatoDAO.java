package model;

import java.sql.*;

public class FileEsportatoDAO {
    public void doSave(FileEsportato file) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO FileEsportato (idScheda, percorso, tipoFile, dataCreazione) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, file.getIdScheda());
            ps.setString(2, file.getPercorso());
            ps.setString(3, file.getTipoFile());
            ps.setTimestamp(4, new Timestamp(file.getDataCreazione().getTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                file.setIdFile(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
