package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;

@WebServlet("/modifyDataServlet")
public class ModifyDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;
        PreferenzeUtente preferenze = new PreferenzeUtenteDAO().doRetrieveByUserId(utente.getIdUser());
        // Recupera tutti i gruppi muscolari unici dal DB
        java.util.List<model.Esercizio> esercizi = new model.EsercizioDAO().doRetrieveAll();
        java.util.Set<String> gruppiMuscolariSet = new java.util.LinkedHashSet<>();
        for (model.Esercizio e : esercizi) {
            gruppiMuscolariSet.add(e.getGruppoMuscolare());
        }
        request.setAttribute("preferenze", preferenze);
        request.setAttribute("gruppiMuscolariList", gruppiMuscolariSet);
        request.getRequestDispatcher("ModifyData.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;
        // Gestione multipla per giorni e gruppi muscolari (checkbox)
        String[] giorniArr = request.getParameterValues("giorniDisponibili");
        String giorniDisponibili = (giorniArr != null) ? String.join(",", giorniArr) : "";
        String[] gruppiArr = request.getParameterValues("gruppiMuscolari");
        String gruppiMuscolari = (gruppiArr != null) ? String.join(",", gruppiArr) : "";
        // Infortuni: radio
        String infortuni = request.getParameter("infortuni");
        String livelloEsperienza = request.getParameter("livelloEsperienza");
        int intensita = 0;
        try { intensita = Integer.parseInt(request.getParameter("intensita")); } catch (Exception e) {}
        PreferenzeUtenteDAO dao = new PreferenzeUtenteDAO();
        PreferenzeUtente preferenze = dao.doRetrieveByUserId(utente.getIdUser());
        if (preferenze == null) {
            preferenze = new PreferenzeUtente();
            preferenze.setIdUser(utente.getIdUser());
        }
        preferenze.setGiorniDisponibili(giorniDisponibili);
        preferenze.setGruppiMuscolari(gruppiMuscolari);
        preferenze.setInfortuni(infortuni);
        preferenze.setLivelloEsperienza(livelloEsperienza);
        preferenze.setIntensita(intensita);
        dao.saveOrUpdate(preferenze);
        // Resta su ModifyData.jsp dopo il salvataggio
        // Aggiorna i dati per la pagina
        // Recupera tutti i gruppi muscolari unici dal DB
        java.util.List<model.Esercizio> esercizi = new model.EsercizioDAO().doRetrieveAll();
        java.util.Set<String> gruppiMuscolariSet = new java.util.LinkedHashSet<>();
        for (model.Esercizio e : esercizi) {
            gruppiMuscolariSet.add(e.getGruppoMuscolare());
        }
        request.setAttribute("preferenze", preferenze);
        request.setAttribute("gruppiMuscolariList", gruppiMuscolariSet);
        request.setAttribute("salvato", true);
        request.getRequestDispatcher("ModifyData.jsp").forward(request, response);
    }
}
