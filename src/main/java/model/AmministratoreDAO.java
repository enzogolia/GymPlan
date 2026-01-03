package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmministratoreDAO {
    public List<Amministratore> doRetrieveAll() {
        List<Amministratore> amministratori = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Amministratore");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Amministratore a = new Amministratore();
                a.setIdAdmin(rs.getInt("idAdmin"));
                a.setUsername(rs.getString("Username"));
                a.setPasswordHash(rs.getString("passwordHash"));
                amministratori.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return amministratori;
    }

    public Amministratore doRetrieveByUsernamePassword(String username, String password) {
        String passwordHash = Hash.hashSHA512(password);
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Amministratore WHERE Username = ? AND passwordHash = ?")) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Amministratore a = new Amministratore();
                a.setIdAdmin(rs.getInt("idAdmin"));
                a.setUsername(rs.getString("Username"));
                a.setPasswordHash(rs.getString("passwordHash"));
                return a;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void doSave(Amministratore amministratore) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO Amministratore (Username, passwordHash) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, amministratore.getUsername());
            ps.setString(2, amministratore.getPasswordHash());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    amministratore.setIdAdmin(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}