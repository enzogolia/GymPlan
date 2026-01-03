package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/executeScheda")
public class ExecuteSessionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action == null || action.equals("start")) {
            startSession(request, response, session);
        } else if (action.equals("saveSet")) {
            saveSet(request, response, session);
        } else if (action.equals("skipSet")) {
            skipSet(request, response, session);
        } else if (action.equals("next")) {
            nextStep(request, response, session);
        } else if (action.equals("finish")) {
            finishSession(request, response, session);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void startSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        int idScheda = Integer.parseInt(request.getParameter("idScheda"));
        
        SchedaAllenamentoDAO schedaDAO = new SchedaAllenamentoDAO();
        SchedaAllenamento scheda = schedaDAO.doRetrieveById(idScheda);
        
        SchedaEsercizioDAO schedaEsercizioDAO = new SchedaEsercizioDAO();
        List<SchedaEsercizio> schedaEsercizi = schedaEsercizioDAO.doRetrieveByIdScheda(idScheda);
        
        EsercizioDAO esercizioDAO = new EsercizioDAO();
        List<Esercizio> esercizi = new ArrayList<>();
        for (SchedaEsercizio se : schedaEsercizi) {
            Esercizio e = esercizioDAO.doRetrieveById(se.getIdEsercizio());
            if (e != null) {
                esercizi.add(e);
            }
        }

        WorkoutSessionState state = new WorkoutSessionState();
        state.setScheda(scheda);
        state.setEsercizi(esercizi);
        
        session.setAttribute("workoutState", state);
        response.sendRedirect(request.getContextPath() + "/execute.jsp");
    }

    private void saveSet(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        WorkoutSessionState state = (WorkoutSessionState) session.getAttribute("workoutState");
        if (state == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int reps = Integer.parseInt(request.getParameter("reps"));
        double weight = Double.parseDouble(request.getParameter("weight"));
        
        Esercizio currentExercise = state.getCurrentExercise();
        if (currentExercise != null) {
            state.addResult(currentExercise.getIdEsercizio(), reps, weight, false);
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void skipSet(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        WorkoutSessionState state = (WorkoutSessionState) session.getAttribute("workoutState");
        if (state == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Esercizio currentExercise = state.getCurrentExercise();
        if (currentExercise != null) {
            state.addResult(currentExercise.getIdEsercizio(), 0, 0, true);
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void nextStep(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        WorkoutSessionState state = (WorkoutSessionState) session.getAttribute("workoutState");
        if (state == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Esercizio currentExercise = state.getCurrentExercise();
        if (currentExercise != null) {
            int currentSet = state.getCurrentSetIndex();
            if (currentSet < currentExercise.getSerieSuggerite()) {
                state.setCurrentSetIndex(currentSet + 1);
            } else {
                state.setCurrentSetIndex(1);
                state.setCurrentExerciseIndex(state.getCurrentExerciseIndex() + 1);
            }
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void finishSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        WorkoutSessionState state = (WorkoutSessionState) session.getAttribute("workoutState");
        if (state == null) {
            response.sendRedirect("profile.jsp");
            return;
        }

        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - state.getStartTime();
        
        // Formattazione durata HH:mm:ss
        long seconds = durationMillis / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        String durationStr = String.format("%02d:%02d:%02d", h, m, s);

        // Salva SessioneAllenamento
        SessioneAllenamento sa = new SessioneAllenamento();
        sa.setIdScheda(state.getScheda().getIdScheda());
        sa.setData(new Date());
        sa.setDurata(durationStr);
        sa.setStato("Completata");
        
        SessioneAllenamentoDAO saDAO = new SessioneAllenamentoDAO();
        int idSessione = saDAO.doSave(sa);

        // Calcola le Statistiche
        int totalReps = 0;
        double totalWeightSum = 0;
        int totalWeightCount = 0;
        
        SerieSvoltaDAO ssDAO = new SerieSvoltaDAO();
        
        for (Map.Entry<Integer, List<WorkoutSessionState.SetResult>> entry : state.getResults().entrySet()) {
            int exerciseId = entry.getKey();
            List<WorkoutSessionState.SetResult> results = entry.getValue();
            
            int exerciseReps = 0;
            double exerciseWeightSum = 0;
            int exerciseWeightCount = 0;
            boolean anySetDone = false;
            
            for (WorkoutSessionState.SetResult res : results) {
                if (!res.skipped) {
                    exerciseReps += res.reps;
                    exerciseWeightSum += res.weight;
                    exerciseWeightCount++;
                    anySetDone = true;
                    
                    totalReps += res.reps;
                    totalWeightSum += res.weight;
                    totalWeightCount++;
                }
            }
            
            double avgWeightExercise = exerciseWeightCount > 0 ? exerciseWeightSum / exerciseWeightCount : 0;
            
            SerieSvolta ss = new SerieSvolta();
            ss.setIdSessione(idSessione);
            ss.setIdEsercizio(exerciseId);
            ss.setRipetizioniEseguite(exerciseReps);
            ss.setEsito(anySetDone ? 1 : 0);
            ss.setPesoUsato(avgWeightExercise);
            
            ssDAO.doSave(ss);
        }

        double globalAvgWeight = totalWeightCount > 0 ? totalWeightSum / totalWeightCount : 0;
        int sessionCount = saDAO.countByScheda(state.getScheda().getIdScheda());

        // Salva ReportAllenamento
        ReportAllenamento ra = new ReportAllenamento();
        ra.setIdSessione(idSessione);
        ra.setRipetizioniTotali(totalReps);
        ra.setPesoMedio(globalAvgWeight);
        ra.setSessioniTotali(sessionCount);
        ra.setDurataMedia(durationStr);
        
        ReportAllenamentoDAO raDAO = new ReportAllenamentoDAO();
        raDAO.doSave(ra);

        // Set degli attributi per la vista del Report
        request.setAttribute("report", ra);
        request.setAttribute("finished", true);
        
        // Pulisce lo stato della sessione
        session.removeAttribute("workoutState");
        
        request.getRequestDispatcher("/execute.jsp").forward(request, response);
    }
}
