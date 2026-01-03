package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/manuale")
public class ManualeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera tutti gli esercizi dal DB
        java.util.List<model.Esercizio> esercizi = new model.EsercizioDAO().doRetrieveAll();
        request.setAttribute("esercizi", esercizi);
        request.getRequestDispatcher("manuale.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof model.Utente)) {
            response.sendRedirect("login");
            return;
        }
        model.Utente utente = (model.Utente) userObj;

        String titolo = request.getParameter("titoloScheda");
        int giorni = 1;
        try { giorni = Integer.parseInt(request.getParameter("giorniScheda")); } catch (Exception e) {}
        int livello = 1;
        try { livello = Integer.parseInt(request.getParameter("livelloScheda")); } catch (Exception e) {}
        int intensita = 1;
        try { intensita = Integer.parseInt(request.getParameter("intensitaScheda")); } catch (Exception e) {}
        String nota = request.getParameter("notaScheda");
        String[] eserciziSelezionati = request.getParameterValues("eserciziSelezionati");
        if (titolo == null || titolo.isEmpty() || giorni < 1 || giorni > 7 || livello < 1 || livello > 3 || intensita < 1 || intensita > 6 || eserciziSelezionati == null || eserciziSelezionati.length == 0) {
            request.setAttribute("errore", "Compila tutti i campi obbligatori e seleziona almeno un esercizio.");
            doGet(request, response);
            return;
        }

        // Focus: gruppi muscolari degli esercizi selezionati
        java.util.List<model.Esercizio> tuttiEsercizi = new model.EsercizioDAO().doRetrieveAll();
        java.util.Set<String> focusSet = new java.util.LinkedHashSet<>();
        for (String idStr : eserciziSelezionati) {
            try {
                int id = Integer.parseInt(idStr);
                for (model.Esercizio e : tuttiEsercizi) {
                    if (e.getIdEsercizio() == id) {
                        focusSet.add(e.getGruppoMuscolare());
                    }
                }
            } catch (Exception ex) {}
        }
        String focus = String.join(" / ", focusSet);

        // Salva la scheda
        model.SchedaAllenamento scheda = new model.SchedaAllenamento();
        scheda.setIdUser(utente.getIdUser());
        scheda.setNome(titolo);
        scheda.setTipo("Manuale");
        scheda.setGiorni(giorni);
        scheda.setFocus(focus);
        scheda.setLivello(livello);
        scheda.setIntensita(intensita);
        scheda.setNote(nota);
        model.SchedaAllenamentoDAO dao = new model.SchedaAllenamentoDAO();
        dao.doSave(scheda);
        int idScheda = dao.doRetrieveIdByNomeTipoUser(titolo, "Manuale", utente.getIdUser());

        // Salva le associazioni Scheda-Esercizio
        model.SchedaEsercizioDAO schedaEsercizioDAO = new model.SchedaEsercizioDAO();
        for (String idStr : eserciziSelezionati) {
            try {
                int id = Integer.parseInt(idStr);
                model.SchedaEsercizio se = new model.SchedaEsercizio();
                se.setIdScheda(idScheda);
                se.setIdEsercizio(id);
                schedaEsercizioDAO.doSave(se);
            } catch (Exception ex) {}
        }

        // Messaggio di successo e redirect
        request.setAttribute("schedaManualeSalvata", true);
        request.setAttribute("titoloScheda", titolo);
        request.setAttribute("idSchedaSalvata", idScheda);
        request.getRequestDispatcher("manuale.jsp").forward(request, response);
    }
}
