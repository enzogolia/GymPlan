package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;
import org.junit.jupiter.api.Test;
// Import necessari per Mockito
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;

class ManualeServletTest {

    @Test
    void testCreazioneManualeFallitaCampiMancanti() throws ServletException, IOException {
        // RIFERIMENTO: TC_1.1.1_1 (Nome vuoto)

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // FIX AGGIUNTO: Creiamo un mock anche per il Dispatcher
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        Utente utente = new Utente();
        utente.setIdUser(1);

        // Setup Sessione Utente
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(utente);

        // FIX AGGIUNTO: Diciamo alla request di restituire il dispatcher finto quando viene chiamato
        // Usiamo anyString() così funziona per qualsiasi pagina JSP ("manuale.jsp", etc.)
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Setup Input Invalido (Nome vuoto, esercizi null)
        when(request.getParameter("titoloScheda")).thenReturn("");
        when(request.getParameter("giorniScheda")).thenReturn("3");

        ManualeServlet servlet = new ManualeServlet();
        servlet.doPost(request, response);

        // Verifica: Deve aver settato l'attributo "errore"
        verify(request).setAttribute(eq("errore"), anyString());

        // Verifica opzionale: controlliamo che abbia provato a ricaricare la pagina
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testRedirectLoginSeNonLoggato() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Nessun utente

        ManualeServlet servlet = new ManualeServlet();
        servlet.doPost(request, response);

        verify(response).sendRedirect("login");
    }
}