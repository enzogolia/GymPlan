package model;

public class Amministratore {
    private int idAdmin;
    private String username;
    private String passwordHash;

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    //altri metodi
    public void setHashAtPassword(String password) {
        this.passwordHash = Hash.hashSHA512(password);
    }

    public boolean validaPassword(String password) {
        return this.passwordHash.equals(Hash.hashSHA512(password));
    }
}