package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;

@WebServlet("/registerUpdateServlet")
public class RegisterUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera username, password ed email dalla richiesta
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Valida i dati: username, password ed email devono rispettare pattern specifici
        if (username == null || !username.matches("^[a-zA-Z0-9_]{1,12}$") ||
                password == null || !password.matches("^(?=(?:.*[A-Za-z]){2,})(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$") ||
                email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {

            // Reindirizza alla pagina di registrazione con un messaggio di errore se la validazione fallisce
            request.setAttribute("error", "Dati inseriti non validi.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Crea un nuovo oggetto utente e imposta i suoi attributi
        UtenteDAO utenteDAO = new UtenteDAO();
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setHashAtPassword(password); // Usa il metodo di Utente per hashare la password
        utente.setEmail(email);

        try {
            // Salva l'utente nel database
            utenteDAO.doSave(utente);

            // Crea una sessione e imposta gli attributi dell'utente
            HttpSession session = request.getSession();
            session.setAttribute("user", utente);
            session.setAttribute("ruolo", "cliente");
            response.sendRedirect("home");
        } catch (RuntimeException e) {
            // Reindirizza alla pagina di registrazione con un messaggio di errore se il salvataggio fallisce
            request.setAttribute("error", "Registrazione fallita. Username o email esistente.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}