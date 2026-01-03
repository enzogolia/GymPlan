package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;

@WebServlet("/salvaScheda")
public class SalvaSchedaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;
        String nome = request.getParameter("nomeScheda");
        // Se il nome contiene un trattino, prende solo la parte dopo il trattino
        if (nome != null && nome.contains("-")) {
            nome = nome.substring(nome.indexOf("-") + 1).trim();
        }
        String tipo = request.getParameter("tipoScheda");
        // Recupera preferenze utente
        PreferenzeUtente preferenze = new PreferenzeUtenteDAO().doRetrieveByUserId(utente.getIdUser());

        // 1) Conta i giorni selezionati
        int giorni = 0;
        if (preferenze != null && preferenze.getGiorniDisponibili() != null && !preferenze.getGiorniDisponibili().isEmpty()) {
            String[] giorniArr = preferenze.getGiorniDisponibili().split(",");
            giorni = giorniArr.length;
        }

        String focus = request.getParameter("focusScheda");

        // 2) Intensità uguale a quella della preferenza
        int intensita = (preferenze != null) ? preferenze.getIntensita() : 1;

        // 3) Livello: Principiante=1, Intermedio=2, Esperto=3
        int livello = 1;
        if (preferenze != null && preferenze.getLivelloEsperienza() != null) {
            String livelloStr = preferenze.getLivelloEsperienza().toLowerCase();
            if (livelloStr.contains("intermedio")) livello = 2;
            else if (livelloStr.contains("esperto")) livello = 3;
            else livello = 1;
        }

        String note = request.getParameter("notaScheda");

        SchedaAllenamento scheda = new SchedaAllenamento();
        scheda.setIdUser(utente.getIdUser());
        scheda.setNome(nome);
        scheda.setTipo(tipo);
        scheda.setGiorni(giorni);
        scheda.setFocus(focus);
        scheda.setLivello(livello);
        scheda.setIntensita(intensita);
        scheda.setNote(note);

        SchedaAllenamentoDAO dao = new SchedaAllenamentoDAO();
        dao.doSave(scheda);

        // Recupera l'id della scheda appena salvata (assumendo che nome, tipo, idUser siano univoci)
        int idScheda = dao.doRetrieveIdByNomeTipoUser(nome, tipo, utente.getIdUser());

        // Salva le associazioni Scheda-Esercizio
        Object eserciziSchedaObj = session.getAttribute("eserciziScheda");
        if (eserciziSchedaObj != null && eserciziSchedaObj instanceof java.util.List) {
            java.util.List<?> eserciziSchedaList = (java.util.List<?>) eserciziSchedaObj;
            model.SchedaEsercizioDAO schedaEsercizioDAO = new model.SchedaEsercizioDAO();
            for (Object esercizioObj : eserciziSchedaList) {
                if (esercizioObj instanceof model.Esercizio) {
                    model.Esercizio esercizio = (model.Esercizio) esercizioObj;
                    model.SchedaEsercizio se = new model.SchedaEsercizio();
                    se.setIdScheda(idScheda);
                    se.setIdEsercizio(esercizio.getIdEsercizio());
                    schedaEsercizioDAO.doSave(se);
                }
            }
        }

        // Salva il file esportato (solo record, no il PDF vero)
        FileEsportato file = new FileEsportato();
        file.setIdScheda(idScheda);
        file.setPercorso("/GymPlan_war_exploded/files/" + nome.replaceAll("\\s+", "_") + ".pdf");
        file.setTipoFile("pdf");
        file.setDataCreazione(new java.util.Date());
        new model.FileEsportatoDAO().doSave(file);

        // Prepara attributi per la pagina di conferma
        request.setAttribute("schedaSalvata", true);
        request.setAttribute("nomeScheda", nome);
        request.setAttribute("percorsoPdf", file.getPercorso());
        request.getRequestDispatcher("generate.jsp").forward(request, response);
    }
}
