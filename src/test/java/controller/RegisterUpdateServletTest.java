package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class RegisterUpdateServletTest {

    @Test
    void testRegistrazioneFallitaPasswordDebole() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("register.jsp")).thenReturn(dispatcher);

        // Input Non Validi (Password troppo corta, niente numeri)
        when(request.getParameter("username")).thenReturn("MarioRossi");
        when(request.getParameter("email")).thenReturn("mario@test.com");
        when(request.getParameter("password")).thenReturn("pippo");

        RegisterUpdateServlet servlet = new RegisterUpdateServlet();
        servlet.doPost(request, response);

        // Verifica che sia stato settato l'errore e fatto il forward
        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}