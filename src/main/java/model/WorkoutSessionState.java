package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutSessionState {
    private SchedaAllenamento scheda;
    private List<Esercizio> esercizi;
    private int currentExerciseIndex;
    private int currentSetIndex;
    private long startTime;
    
    // Map<ExerciseID, List<SetResult>>
    // We need a small inner class or structure to hold set results before aggregation
    private Map<Integer, List<SetResult>> results;

    public WorkoutSessionState() {
        this.esercizi = new ArrayList<>();
        this.results = new HashMap<>();
        this.currentExerciseIndex = 0;
        this.currentSetIndex = 1; // 1-based for display
        this.startTime = System.currentTimeMillis();
    }

    public static class SetResult {
        public int reps;
        public double weight;
        public boolean skipped;

        public SetResult(int reps, double weight, boolean skipped) {
            this.reps = reps;
            this.weight = weight;
            this.skipped = skipped;
        }
    }

    public void addResult(int exerciseId, int reps, double weight, boolean skipped) {
        results.computeIfAbsent(exerciseId, k -> new ArrayList<>()).add(new SetResult(reps, weight, skipped));
    }

    public SchedaAllenamento getScheda() {
        return scheda;
    }

    public void setScheda(SchedaAllenamento scheda) {
        this.scheda = scheda;
    }

    public List<Esercizio> getEsercizi() {
        return esercizi;
    }

    public void setEsercizi(List<Esercizio> esercizi) {
        this.esercizi = esercizi;
    }

    public int getCurrentExerciseIndex() {
        return currentExerciseIndex;
    }

    public void setCurrentExerciseIndex(int currentExerciseIndex) {
        this.currentExerciseIndex = currentExerciseIndex;
    }

    public int getCurrentSetIndex() {
        return currentSetIndex;
    }

    public void setCurrentSetIndex(int currentSetIndex) {
        this.currentSetIndex = currentSetIndex;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Map<Integer, List<SetResult>> getResults() {
        return results;
    }
    
    public Esercizio getCurrentExercise() {
        if (currentExerciseIndex < esercizi.size()) {
            return esercizi.get(currentExerciseIndex);
        }
        return null;
    }
}
