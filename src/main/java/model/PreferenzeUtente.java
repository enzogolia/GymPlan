package model;

public class PreferenzeUtente {
    private int idPreferenza;
    private int idUser;
    private String giorniDisponibili;
    private String gruppiMuscolari;
    private String infortuni;
    private String livelloEsperienza;
    private int intensita;

    public int getIdPreferenza() {
        return idPreferenza;
    }

    public void setIdPreferenza(int idPreferenza) {
        this.idPreferenza = idPreferenza;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getGiorniDisponibili() {
        return giorniDisponibili;
    }

    public void setGiorniDisponibili(String giorniDisponibili) {
        this.giorniDisponibili = giorniDisponibili;
    }

    public String getGruppiMuscolari() {
        return gruppiMuscolari;
    }

    public void setGruppiMuscolari(String gruppiMuscolari) {
        this.gruppiMuscolari = gruppiMuscolari;
    }

    public String getInfortuni() {
        return infortuni;
    }

    public void setInfortuni(String infortuni) {
        this.infortuni = infortuni;
    }

    public String getLivelloEsperienza() {
        return livelloEsperienza;
    }

    public void setLivelloEsperienza(String livelloEsperienza) {
        this.livelloEsperienza = livelloEsperienza;
    }

    public int getIntensita() {
        return intensita;
    }

    public void setIntensita(int intensita) {
        this.intensita = intensita;
    }
}