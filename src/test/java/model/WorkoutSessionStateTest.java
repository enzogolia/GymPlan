package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutSessionStateTest {

    private WorkoutSessionState state;
    private List<Esercizio> esercizi;

    @BeforeEach
    void setUp() {
        state = new WorkoutSessionState();
        esercizi = new ArrayList<>();

        // Mocking dati esercizi
        Esercizio e1 = new Esercizio();
        e1.setIdEsercizio(1);
        e1.setNome("Panca Piana");
        e1.setSerieSuggerite(3); // 3 Serie

        Esercizio e2 = new Esercizio();
        e2.setIdEsercizio(2);
        e2.setNome("Squat");
        e2.setSerieSuggerite(2); // 2 Serie

        esercizi.add(e1);
        esercizi.add(e2);

        state.setEsercizi(esercizi);
    }

    @Test
    void testInizializzazione() {
        assertEquals(0, state.getCurrentExerciseIndex(), "Deve iniziare dal primo esercizio");
        assertEquals(1, state.getCurrentSetIndex(), "Deve iniziare dalla prima serie");
        assertNotNull(state.getCurrentExercise(), "L'esercizio corrente non deve essere null");
        assertEquals("Panca Piana", state.getCurrentExercise().getNome());
    }

    @Test
    void testAddResult() {
        // Simula il salvataggio di una serie
        state.addResult(1, 10, 50.0, false);

        var results = state.getResults().get(1);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(10, results.get(0).reps);
        assertEquals(50.0, results.get(0).weight);
        assertFalse(results.get(0).skipped);
    }

    @Test
    void testNavigazioneProssimaSerie() {
        // Siamo all'esercizio 1 (3 serie), serie 1
        state.setCurrentSetIndex(1);
        state.setCurrentExerciseIndex(0);

        // Simuliamo logica nextStep (manuale qui, nel controller è automatica)
        int currentSet = state.getCurrentSetIndex();
        int maxSets = state.getCurrentExercise().getSerieSuggerite();

        // Logica replicata dal Controller per il test
        if (currentSet < maxSets) {
            state.setCurrentSetIndex(currentSet + 1);
        }

        assertEquals(2, state.getCurrentSetIndex(), "Dovrebbe passare alla serie 2");
        assertEquals(0, state.getCurrentExerciseIndex(), "Dovrebbe rimanere sullo stesso esercizio");
    }
}