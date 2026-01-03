package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet("/generate")
public class GenerateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof model.Utente)) {
            response.sendRedirect("login");
            return;
        }
        model.Utente utente = (model.Utente) userObj;
        model.PreferenzeUtente preferenze = new model.PreferenzeUtenteDAO().doRetrieveByUserId(utente.getIdUser());
        List<model.Esercizio> esercizi = new model.EsercizioDAO().doRetrieveAll();

        // Gruppi muscolari preferiti
        Set<String> gruppiPreferiti = new LinkedHashSet<>();
        if (preferenze.getGruppiMuscolari() != null && !preferenze.getGruppiMuscolari().isEmpty()) {
            for (String g : preferenze.getGruppiMuscolari().split(",")) {
                gruppiPreferiti.add(g.trim());
            }
        }
        // Intensità: 1=4, 2=5, ..., 6=10
        int intensita = preferenze.getIntensita();
        if (intensita < 1) intensita = 1;
        if (intensita > 6) intensita = 6;
        int numEsercizi = 4 + (intensita - 1);
        if (numEsercizi > 10) numEsercizi = 10;

        // Livello
        String livello = preferenze.getLivelloEsperienza();

        // Solo esercizi dei gruppi muscolari preferiti
        List<model.Esercizio> candidati = new ArrayList<>();
        for (model.Esercizio e : esercizi) {
            String gruppo = e.getGruppoMuscolare();
            if (gruppiPreferiti.contains(gruppo)) {
                candidati.add(e);
            }
        }

        // Se meno esercizi disponibili, prendi tutti
        if (candidati.size() < numEsercizi) numEsercizi = candidati.size();
        // Seleziona casualmente gli esercizi
        Collections.shuffle(candidati);
        List<model.Esercizio> eserciziScheda = candidati.subList(0, numEsercizi);

        // Note per livello
        String notaPrincipiante = "Concentrati sulla tecnica e non sul carico.";
        String notaIntermedio = "Incrementa gradualmente il carico e varia gli esercizi.";
        String notaEsperto = "Spingi al massimo, cura la progressione e la qualità.";
        String notaScheda = "";
        if ("Principiante".equalsIgnoreCase(livello)) {
            notaScheda = notaPrincipiante;
        } else if ("Intermedio".equalsIgnoreCase(livello)) {
            notaScheda = notaIntermedio;
        } else if ("Esperto".equalsIgnoreCase(livello)) {
            notaScheda = notaEsperto;
        } else {
            notaScheda = "Scheda personalizzata.";
        }

        // Focus: gruppi muscolari degli esercizi scelti
        Set<String> focusSet = new LinkedHashSet<>();
        for (model.Esercizio e : eserciziScheda) {
            focusSet.add(e.getGruppoMuscolare());
        }
        String focus = String.join(" / ", focusSet);

        // Titolo: Scheda Generata Correttamente - Scheda Principiante/Intermedia/Esperta
        String titoloScheda = "Scheda Generata Correttamente - Scheda ";
        if ("Principiante".equalsIgnoreCase(livello)) {
            titoloScheda += "Principiante";
        } else if ("Intermedio".equalsIgnoreCase(livello)) {
            titoloScheda += "Intermedia";
        } else if ("Esperto".equalsIgnoreCase(livello)) {
            titoloScheda += "Esperta";
        } else {
            titoloScheda += "Personalizzata";
        }

        request.setAttribute("eserciziScheda", eserciziScheda);
        session.setAttribute("eserciziScheda", eserciziScheda);
        request.setAttribute("notaScheda", notaScheda);
        request.setAttribute("focus", focus);
        request.setAttribute("titoloScheda", titoloScheda);
        request.getRequestDispatcher("generate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
