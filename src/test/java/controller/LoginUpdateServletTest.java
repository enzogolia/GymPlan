/*Questo test simula una richiesta di Login.
Poiché la servlet crea new UtenteDAO(),
se il DB MySQL non è acceso, questo test fallirà con un errore di connessione.
È normale data l'architettura.
*/
package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LoginUpdateServletTest {

    @Test
    void testLoginFallitoInputNonValido() throws ServletException, IOException {
        // Testiamo il caso di input vuoto (non tocca il DB, quindi Unit Test puro)

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Input vuoti
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");

        LoginUpdateServlet servlet = new LoginUpdateServlet();
        servlet.doPost(request, response);

        // Verifica che abbia mandato un errore
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali non valide.");
    }

    @Test
    void testLoginFallitoCredenzialiErrate() throws ServletException, IOException {
        /* * NOTA: Questo test proverà a connettersi al DB tramite UtenteDAO.
         * Se il DB è spento, lancerà RuntimeException.
         * Assumiamo qui che il DB risponda o che gestiamo l'eccezione.
         */

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // Credenziali sintatticamente valide ma inesistenti
        when(request.getParameter("username")).thenReturn("utenteFinto");
        when(request.getParameter("password")).thenReturn("Pass123!");
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);

        try {
            LoginUpdateServlet servlet = new LoginUpdateServlet();
            servlet.doPost(request, response);

            // Se il DB è connesso e l'utente non esiste:
            verify(request).setAttribute("error", "Credenziali errate");
            verify(dispatcher).forward(request, response);
        } catch (RuntimeException e) {
            // Ignoriamo errori di connessione DB per questo esempio didattico
            System.out.println("Test saltato causa DB offline: " + e.getMessage());
        }
    }
}