package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;
        List<ReportAllenamento> reportList = new ReportAllenamentoDAO().doRetrieveByUserId(utente.getIdUser());
        request.setAttribute("reportAllenamentoUtente", reportList);
        request.getRequestDispatcher("report.jsp").forward(request, response);
    }
}
