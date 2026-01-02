package model;

import java.util.Date;

public class SessioneAllenamento {
    private int idSessione;
    private int idScheda;
    private Date data;
    private String durata;
    private String stato;

    public int getIdSessione() {
        return idSessione;
    }

    public void setIdSessione(int idSessione) {
        this.idSessione = idSessione;
    }

    public int getIdScheda() {
        return idScheda;
    }

    public void setIdScheda(int idScheda) {
        this.idScheda = idScheda;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}