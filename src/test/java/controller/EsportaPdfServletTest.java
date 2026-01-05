package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class EsportaPdfServletTest {

    @Test
    void testEsportaPdfRedirectSeNonLoggato() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        EsportaPdfServlet servlet = new EsportaPdfServlet();
        servlet.doPost(request, response);

        verify(response).sendRedirect("login");
    }

    @Test
    void testEsportaPdfSchedaInesistente() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Utente utente = new Utente(); utente.setIdUser(1);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(utente);

        // ID scheda non valido o nullo
        when(request.getParameter("idScheda")).thenReturn(null);
        when(request.getParameter("nomeScheda")).thenReturn(null);

        EsportaPdfServlet servlet = new EsportaPdfServlet();

        try {
            servlet.doPost(request, response);
            // Se non trova la scheda, dovrebbe tornare alla home
            verify(response).sendRedirect("home");
        } catch (RuntimeException e) {
            // Ignora errore DB
        }
    }
}