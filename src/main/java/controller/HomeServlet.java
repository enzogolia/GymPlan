package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera gli esercizi dal contesto dell'applicazione
        List<Esercizio> esercizi = (List<Esercizio>) getServletContext().getAttribute("esercizi");
        request.setAttribute("esercizi", esercizi);

        // Recupera l'utente dalla sessione
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("user") : null;
        request.setAttribute("user", user);

        // Recupera il ruolo dell'utente dalla sessione
        String ruolo = (session != null) ? (String) session.getAttribute("ruolo") : null;
        request.setAttribute("ruolo", ruolo);

        // Recupera solo le schede dell'utente loggato (se utente normale)
        List<SchedaAllenamento> schedeUtente = null;
        if (user != null && "utente".equals(ruolo)) {
            int idUser = ((model.Utente) user).getIdUser();
            SchedaAllenamentoDAO schedaDAO = new SchedaAllenamentoDAO();
            schedeUtente = schedaDAO.doRetrieveByUserId(idUser);
        }
        request.setAttribute("schedeAllenamentoUtente", schedeUtente);

        // Controlla se ci sono errori
        if (esercizi == null || esercizi.isEmpty()) {
            request.setAttribute("errorDB", "Nessun esercizio disponibile al momento.");
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}

