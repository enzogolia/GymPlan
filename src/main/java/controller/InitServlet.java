package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import java.util.*;

@WebServlet(name = "MyInit", urlPatterns = "/MyInit", loadOnStartup = 0)
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // Carica gli esercizi dal database
        EsercizioDAO esercizioDAO = new EsercizioDAO();
        List<Esercizio> esercizi = esercizioDAO.doRetrieveAll();
        getServletContext().setAttribute("esercizi", esercizi);

        // Carica le schede di allenamento dal database
        SchedaAllenamentoDAO schedaDAO = new SchedaAllenamentoDAO();
        List<SchedaAllenamento> schede = schedaDAO.doRetrieveAll();
        getServletContext().setAttribute("schedeAllenamento", schede);

        super.init();
    }
}

