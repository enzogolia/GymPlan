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
    private String livello;   // Modificato: String (Principiante, Intermedio, Esperto)
    private String immagine;  // Nuovo campo per il nome del file .jpg

    // --- GETTER E SETTER STANDARD ---

    public int getIdEsercizio() {
        return idEsercizio;
    }

    public void setIdEsercizio(int idEsercizio) {
        this.idEsercizio = idEsercizio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGruppoMuscolare() {
        return gruppoMuscolare;
    }

    public void setGruppoMuscolare(String gruppoMuscolare) {
        this.gruppoMuscolare = gruppoMuscolare;
    }

    public int getSerieSuggerite() {
        return serieSuggerite;
    }

    public void setSerieSuggerite(int serieSuggerite) {
        this.serieSuggerite = serieSuggerite;
    }

    public int getRipetizioni() {
        return ripetizioni;
    }

    public void setRipetizioni(int ripetizioni) {
        this.ripetizioni = ripetizioni;
    }

    public int getCaricoSuggerito() {
        return caricoSuggerito;
    }

    public void setCaricoSuggerito(int caricoSuggerito) {
        this.caricoSuggerito = caricoSuggerito;
    }

    // --- GESTIONE RECUPERO (Time -> String) ---

    public String getRecupero() {
        return recupero;
    }

    public void setRecupero(Time recupero) {
        if (recupero != null) {
            this.recupero = recupero.toString();
        } else {
            this.recupero = "00:01:30"; // Valore di default se null nel DB
        }
    }

    // --- GESTIONE LIVELLO ---

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    // --- GESTIONE IMMAGINE (Smart Logic) ---

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }


    public String getImmagine() {
        // 1. Se esiste un'immagine specifica (es. "panca_piana.jpg"), usala.
        if (immagine != null && !immagine.trim().isEmpty()) {
            return immagine;
        }

        // 2. Altrimenti, usa l'immagine del gruppo muscolare (es. "Petto.jpg").
        if (gruppoMuscolare != null && !gruppoMuscolare.trim().isEmpty()) {
            return gruppoMuscolare + ".jpg";
        }

        // 3. Fallback finale se non c'è nemmeno il gruppo muscolare.
        return "default.jpg";
    }
}