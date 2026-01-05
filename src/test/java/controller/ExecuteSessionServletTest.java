package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Esercizio;
import model.SchedaAllenamento;
import model.WorkoutSessionState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ExecuteSessionServletTest {

    @Test
    void testSalvaSerieSuccesso() throws ServletException, IOException {
        // RIFERIMENTO: TC_1.3.2_2 (Inserimento dati validi)

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // Prepariamo lo stato della sessione (Mocking complesso)
        WorkoutSessionState state = new WorkoutSessionState();
        List<Esercizio> esercizi = new ArrayList<>();
        Esercizio e = new Esercizio();
        e.setIdEsercizio(1);
        e.setSerieSuggerite(3);
        esercizi.add(e);
        state.setEsercizi(esercizi);

        // Configurazione Mock
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("saveSet");
        when(request.getParameter("reps")).thenReturn("10");
        when(request.getParameter("weight")).thenReturn("50");
        when(session.getAttribute("workoutState")).thenReturn(state);

        ExecuteSessionServlet servlet = new ExecuteSessionServlet();
        servlet.doPost(request, response);

        // Verifica che abbia risposto OK (200)
        verify(response).setStatus(HttpServletResponse.SC_OK);
        // Verifica che nel modello i dati siano stati salvati
        assert !state.getResults().get(1).isEmpty();
        assert state.getResults().get(1).get(0).reps == 10;
    }

    @Test
    void testSaltaSerie() throws ServletException, IOException {
        // RIFERIMENTO: Logica di Skip Set

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        WorkoutSessionState state = new WorkoutSessionState();
        List<Esercizio> esercizi = new ArrayList<>();
        Esercizio e = new Esercizio(); e.setIdEsercizio(1);
        esercizi.add(e);
        state.setEsercizi(esercizi);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("action")).thenReturn("skipSet");
        when(session.getAttribute("workoutState")).thenReturn(state);

        ExecuteSessionServlet servlet = new ExecuteSessionServlet();
        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        // Verifica che sia stato salvato come skipped (esito = saltata)
        assert state.getResults().get(1).get(0).skipped == true;
    }
}