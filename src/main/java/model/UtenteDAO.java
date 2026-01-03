package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {
    public List<Utente> doRetrieveAll() {
        List<Utente> utenti = new ArrayList<>();
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Utente");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utente u = new Utente();
                u.setIdUser(rs.getInt("IdUser"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setNome(rs.getString("nome"));
                u.setCognome(rs.getString("cognome"));
                u.setEta(rs.getInt("eta"));
                u.setPeso(rs.getInt("peso"));
                u.setEmail(rs.getString("email"));
                utenti.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;
    }

    public Utente doRetrieveByUsernamePassword(String username, String password) {
        String passwordHash = Hash.hashSHA512(password);
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Utente WHERE Username = ? AND PasswordHash = ?")) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Utente u = new Utente();
                u.setIdUser(rs.getInt("IdUser"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setNome(rs.getString("nome"));
                u.setCognome(rs.getString("cognome"));
                u.setEta(rs.getInt("eta"));
                u.setPeso(rs.getInt("peso"));
                u.setEmail(rs.getString("email"));
                return u;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void doSave(Utente utente) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO Utente (Username, PasswordHash, nome, cognome, eta, peso, email) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utente.getUsername());
            ps.setString(2, utente.getPasswordHash());
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getCognome());
            ps.setInt(5, utente.getEta());
            ps.setInt(6, utente.getPeso());
            ps.setString(7, utente.getEmail());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    utente.setIdUser(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Utente utente) {
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Utente SET PasswordHash = ?, nome = ?, cognome = ?, eta = ?, peso = ?, email = ? WHERE Username = ?")) {

            ps.setString(1, utente.getPasswordHash());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setInt(4, utente.getEta());
            ps.setInt(5, utente.getPeso());
            ps.setString(6, utente.getEmail());
            ps.setString(7, utente.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
