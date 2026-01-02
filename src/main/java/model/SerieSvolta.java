package model;

public class SerieSvolta {
    private int idSessione;
    private int idEsercizio;
    private int ripetizioniEseguite;
    private int esito; // 1 = eseguita, 0 = saltata
    private double pesoUsato;

    public int getIdSessione() {
        return idSessione;
    }

    public void setIdSessione(int idSessione) {
        this.idSessione = idSessione;
    }

    public int getIdEsercizio() {
        return idEsercizio;
    }

    public void setIdEsercizio(int idEsercizio) {
        this.idEsercizio = idEsercizio;
    }

    public int getRipetizioniEseguite() {
        return ripetizioniEseguite;
    }

    public void setRipetizioniEseguite(int ripetizioniEseguite) {
        this.ripetizioniEseguite = ripetizioniEseguite;
    }

    public int getEsito() {
        return esito;
    }

    public void setEsito(int esito) {
        this.esito = esito;
    }

    public double getPesoUsato() {
        return pesoUsato;
    }

    public void setPesoUsato(double pesoUsato) {
        this.pesoUsato = pesoUsato;
    }
}