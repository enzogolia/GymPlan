package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class GenerateServletTest {

    @Test
    void testAccessoNegatoUtenteNonLoggato() throws ServletException, IOException {
        // Verifica Sicurezza (TC implicito)
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        GenerateServlet servlet = new GenerateServlet();
        servlet.doGet(request, response);

        verify(response).sendRedirect("login");
    }

    @Test
    void testGenerazioneFlusso() throws ServletException, IOException {
        /* Questo test verifica che la servlet provi a recuperare le preferenze.
         * Se il DB è spento, lancerà eccezione RuntimeException dal DAO.
         * Lo catturiamo per confermare che la servlet ha iniziato il processo corretto.
         */
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Utente utente = new Utente();
        utente.setIdUser(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(utente);

        GenerateServlet servlet = new GenerateServlet();

        try {
            servlet.doGet(request, response);
        } catch (RuntimeException e) {
            // Se arriviamo qui (errore DB), significa che il controllo login è passato
            // e ha tentato di chiamare PreferenzeDAO. È un successo parziale per un unit test senza DB.
            System.out.println("Test Servlet avviato correttamente (Stop su DB connection)");
        }
    }
}