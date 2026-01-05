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

class LoginServletTest {

    @Test
    void testDoGet() throws ServletException, IOException {
        // 1. Mocking degli oggetti Servlet
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // 2. Comportamento atteso
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        // 3. Esecuzione
        LoginServlet servlet = new LoginServlet();
        servlet.doGet(request, response);

        // 4. Verifica (Verify)
        // Verifichiamo che la servlet abbia chiamato il dispatcher corretto
        verify(request, times(1)).getRequestDispatcher("/login.jsp");
        verify(dispatcher, times(1)).forward(request, response);
    }
}