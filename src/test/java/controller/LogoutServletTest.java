package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LogoutServletTest {

    @Test
    void testLogout() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // Quando viene richiesta la sessione, restituisci il mock
        when(request.getSession(false)).thenReturn(session);

        LogoutServlet servlet = new LogoutServlet();
        servlet.doGet(request, response);

        // Verifica che la sessione sia stata invalidata
        verify(session, times(1)).invalidate();
        // Verifica il redirect
        verify(response, times(1)).sendRedirect("index.jsp");
    }
}