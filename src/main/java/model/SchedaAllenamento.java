package model;

public class SchedaAllenamento {
    private int idScheda;
    private int idUser;
    private String nome;
    private String tipo;
    private int giorni;
    private String focus;
    private int livello;
    private int intensita;
    private String note;

    public int getIdScheda() {
        return idScheda;
    }

    public void setIdScheda(int idScheda) {
        this.idScheda = idScheda;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getGiorni() {
        return giorni;
    }

    public void setGiorni(int giorni) {
        this.giorni = giorni;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public int getIntensita() {
        return intensita;
    }

    public void setIntensita(int intensita) {
        this.intensita = intensita;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}