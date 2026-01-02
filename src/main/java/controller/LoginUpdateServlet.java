package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import java.io.IOException;

@WebServlet("/loginUpdateServlet")
public class LoginUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera username e password dalla richiesta
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Valida i dati: l'username deve rispettare un pattern specifico e la password non deve essere vuota
        if (username == null || !username.matches("^[a-zA-Z0-9_]{1,12}$") ||
                password == null || password.isEmpty()) {
            // Invia una risposta di errore se la validazione fallisce
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali non valide.");
            return;
        }

        // Controlla se l'utente esiste nel database come cliente
        UtenteDAO utenteDAO = new UtenteDAO();
        Utente utente = utenteDAO.doRetrieveByUsernamePassword(username, password);

        if (utente != null) {
            // Se il cliente esiste, crea una sessione e imposta gli attributi dell'utente
            HttpSession session = request.getSession();
            session.setAttribute("user", utente);
            session.setAttribute("ruolo", "utente");
            session.setAttribute("loggedIn", true); // Imposta l'attributo loggedIn per l'utente loggato
            response.sendRedirect("home");
            return;
        }

        // Controlla se l'utente esiste nel database come amministratore
        AmministratoreDAO amministratoreDAO = new AmministratoreDAO();
        Amministratore amministratore = amministratoreDAO.doRetrieveByUsernamePassword(username, password);

        if (amministratore != null) {
            // Se l'amministratore esiste, crea una sessione e imposta gli attributi dell'utente
            HttpSession session = request.getSession();
            session.setAttribute("user", amministratore);
            session.setAttribute("ruolo", "amministratore");
            session.setAttribute("loggedIn", true); // Imposta l'attributo loggedIn per l'utente loggato
            response.sendRedirect("home");
            return;
        }

        // Se nessun utente viene trovato, reindirizza alla pagina di login con un messaggio di errore
        request.setAttribute("error", "Credenziali errate");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
