package model;

public class StoricoSchede {
    private int idStoricoScheda;
    private int idUser;
    private String dataCreazione;
    private String tipoAllenamento;
    private String stato;

    public int getIdStoricoScheda() {
        return idStoricoScheda;
    }

    public void setIdStoricoScheda(int idStoricoScheda) {
        this.idStoricoScheda = idStoricoScheda;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getTipoAllenamento() {
        return tipoAllenamento;
    }

    public void setTipoAllenamento(String tipoAllenamento) {
        this.tipoAllenamento = tipoAllenamento;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}