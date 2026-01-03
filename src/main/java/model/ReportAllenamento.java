package model;

public class ReportAllenamento {
    private int idSessione;
    private int ripetizioniTotali;
    private double pesoMedio;
    private int sessioniTotali;
    private String durataMedia;
    private String nomeScheda;
    public String getNomeScheda() {
        return nomeScheda;
    }

    public void setNomeScheda(String nomeScheda) {
        this.nomeScheda = nomeScheda;
    }

    public int getIdSessione() {
        return idSessione;
    }

    public void setIdSessione(int idSessione) {
        this.idSessione = idSessione;
    }

    public int getRipetizioniTotali() {
        return ripetizioniTotali;
    }

    public void setRipetizioniTotali(int ripetizioniTotali) {
        this.ripetizioniTotali = ripetizioniTotali;
    }

    public double getPesoMedio() {
        return pesoMedio;
    }

    public void setPesoMedio(double pesoMedio) {
        this.pesoMedio = pesoMedio;
    }

    public int getSessioniTotali() {
        return sessioniTotali;
    }

    public void setSessioniTotali(int sessioniTotali) {
        this.sessioniTotali = sessioniTotali;
    }

    public String getDurataMedia() {
        return durataMedia;
    }

    public void setDurataMedia(String durataMedia) {
        this.durataMedia = durataMedia;
    }
}