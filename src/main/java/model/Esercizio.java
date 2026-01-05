package model;

import java.sql.Time;

public class Esercizio {
    private int idEsercizio;
    private String nome;
    private String gruppoMuscolare;
    private int serieSuggerite;
    private int ripetizioni;
    private int caricoSuggerito;
    private String recupero;
    private String livello; // <--- NUOVO CAMPO STRINGA

    public int getIdEsercizio() { return idEsercizio; }
    public void setIdEsercizio(int idEsercizio) { this.idEsercizio = idEsercizio; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getGruppoMuscolare() { return gruppoMuscolare; }
    public void setGruppoMuscolare(String gruppoMuscolare) { this.gruppoMuscolare = gruppoMuscolare; }

    public int getSerieSuggerite() { return serieSuggerite; }
    public void setSerieSuggerite(int serieSuggerite) { this.serieSuggerite = serieSuggerite; }

    public int getRipetizioni() { return ripetizioni; }
    public void setRipetizioni(int ripetizioni) { this.ripetizioni = ripetizioni; }

    public int getCaricoSuggerito() { return caricoSuggerito; }
    public void setCaricoSuggerito(int caricoSuggerito) { this.caricoSuggerito = caricoSuggerito; }

    public String getRecupero() { return recupero; }

    public void setRecupero(Time recupero) {
        if (recupero != null) {
            this.recupero = recupero.toString();
        } else {
            this.recupero = "00:01:30";
        }
    }

    // --- GETTER E SETTER PER IL LIVELLO ---
    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }
}