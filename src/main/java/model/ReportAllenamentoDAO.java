package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportAllenamentoDAO {
    public List<ReportAllenamento> doRetrieveByUserId(int idUser) {
        List<ReportAllenamento> reports = new ArrayList<>();
        String sql = "SELECT r.idSessione, r.ripetizioniTotali, r.pesoMedio, r.sessioniTotali, r.durataMedia, sa.nome AS nomeScheda " +
                 "FROM ReportAllenamento r " +
                 "JOIN SessioneAllenamento s ON r.idSessione = s.idSessione " +
                 "JOIN SchedaAllenamento sa ON s.IdScheda = sa.IdScheda " +
                 "WHERE sa.IdUser = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReportAllenamento r = new ReportAllenamento();
                r.setIdSessione(rs.getInt("idSessione"));
                r.setRipetizioniTotali(rs.getInt("ripetizioniTotali"));
                r.setPesoMedio(rs.getDouble("pesoMedio"));
                r.setSessioniTotali(rs.getInt("sessioniTotali"));
                r.setDurataMedia(rs.getString("durataMedia"));
                // nomeScheda
                try {
                    java.lang.reflect.Field f = r.getClass().getDeclaredField("nomeScheda");
                    f.setAccessible(true);
                    f.set(r, rs.getString("nomeScheda"));
                } catch (Exception e) { /* se il campo non esiste, ignora */ }
                reports.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }

    public void doSave(ReportAllenamento r) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO ReportAllenamento (idSessione, ripetizioniTotali, pesoMedio, sessioniTotali, durataMedia) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, r.getIdSessione());
            ps.setInt(2, r.getRipetizioniTotali());
            ps.setDouble(3, r.getPesoMedio());
            ps.setInt(4, r.getSessioniTotali());
            ps.setString(5, r.getDurataMedia());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
