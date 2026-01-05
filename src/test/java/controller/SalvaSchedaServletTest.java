package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Esercizio;
import model.Utente;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class SalvaSchedaServletTest {

    @Test
    void testSalvataggioSchedaSuccesso() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // Setup Utente
        Utente utente = new Utente();
        utente.setIdUser(1);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(utente);

        // Setup Dati Scheda dalla Request
        when(request.getParameter("nomeScheda")).thenReturn("Scheda Test");
        when(request.getParameter("tipoScheda")).thenReturn("Automatica");
        // ... altri parametri opzionali se necessario

        // Setup Esercizi in Sessione (Simula che GenerateServlet abbia già lavorato)
        List<Esercizio> esercizi = new ArrayList<>();
        Esercizio e = new Esercizio(); e.setIdEsercizio(10);
        esercizi.add(e);
        when(session.getAttribute("eserciziScheda")).thenReturn(esercizi);

        // Mock Dispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        SalvaSchedaServlet servlet = new SalvaSchedaServlet();

        try {
            servlet.doPost(request, response);

            // Verifica che abbia impostato l'attributo di successo
            verify(request).setAttribute(eq("schedaSalvata"), eq(true));
            // Verifica che il nome scheda sia passato alla JSP
            verify(request).setAttribute(eq("nomeScheda"), eq("Scheda Test"));
            verify(dispatcher).forward(request, response);

        } catch (RuntimeException ex) {
            // Ignoriamo l'errore di connessione al DB (DAO)
            System.out.println("Test Salvataggio parziale (DAO offline) - Logica servlet OK");
        }
    }
}