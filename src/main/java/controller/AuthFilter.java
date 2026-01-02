package controller;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class AuthFilter implements Filter {
    //URL che non richiedono autenticazione
    private static final List<String> publicPaths = Arrays.asList(
            "/home",
            "/login",
            "/register",
            "/registerUpdateServlet",
            "/loginUpdateServlet",
            "/exercise",
            "/css/",
            "/images/",
            "/js/",
            "/fonts/"
    );


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        if (session.getAttribute("loggedIn") == null) {
            session.setAttribute("loggedIn", false);
        }

        //Set loggedIn per ogni request
        if (session != null) {
            Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
            if (loggedIn == null) {
                loggedIn = (session.getAttribute("user") != null);
                session.setAttribute("loggedIn", loggedIn);
            }
        }

        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean isLoggedIn = (session != null) && (Boolean.TRUE.equals(session.getAttribute("loggedIn")));
        String role = (session != null) ? (String) session.getAttribute("ruolo") : null;

        boolean isAdminPage = path.contains("adminPanel");
        boolean isListSection = path.contains("listaSchede");

        boolean isPublic = publicPaths.stream().anyMatch(path::startsWith);


        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }


        if (isAdminPage && !"amministratore".equals(role)) {
            response.sendError(403, "Accesso non autorizzato.");
            return;
        }


        if (isListSection && "amministratore".equals(role)) {
            response.sendError(403, "Gli amministratori non possono accedere alla lista.");
            return;
        }


        chain.doFilter(req, res);
    }
}

