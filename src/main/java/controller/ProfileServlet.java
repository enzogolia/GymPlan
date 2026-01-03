package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof model.Utente)) {
            response.sendRedirect("login");
            return;
        }
        model.Utente utente = (model.Utente) userObj;
        request.setAttribute("username", utente.getUsername());
        request.setAttribute("nome", utente.getNome());
        request.setAttribute("cognome", utente.getCognome());
        request.setAttribute("email", utente.getEmail());
        request.setAttribute("peso", utente.getPeso());
        request.setAttribute("eta", utente.getEta());
        // Recupera le schede dell'utente
        List<model.SchedaAllenamento> schedeUtente = new model.SchedaAllenamentoDAO().doRetrieveByUserId(utente.getIdUser());
        request.setAttribute("schedeAllenamentoUtente", schedeUtente);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
