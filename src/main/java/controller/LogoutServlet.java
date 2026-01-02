package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalida la sessione
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Reindirizza alla pagina di home
        response.sendRedirect("index.jsp");
    }
}
