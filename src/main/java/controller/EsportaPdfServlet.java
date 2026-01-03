package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/esportaPdf")
public class EsportaPdfServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;

        // Supporta export sia da generate.jsp (nomeScheda) che da manuale.jsp (idScheda)
        String nomeScheda = request.getParameter("nomeScheda");
        String idSchedaParam = request.getParameter("idScheda");
        SchedaAllenamento scheda = null;
        int idScheda = -1;
        SchedaAllenamentoDAO dao = new SchedaAllenamentoDAO();
        if (idSchedaParam != null && !idSchedaParam.isEmpty()) {
            try {
                idScheda = Integer.parseInt(idSchedaParam);
                scheda = dao.doRetrieveById(idScheda);
            } catch (Exception e) { idScheda = -1; }
        } else if (nomeScheda != null && !nomeScheda.isEmpty()) {
            idScheda = dao.doRetrieveIdByNomeTipoUser(nomeScheda, "Automatica", utente.getIdUser());
            List<SchedaAllenamento> schede = dao.doRetrieveByUserId(utente.getIdUser());
            for (SchedaAllenamento s : schede) {
                if (s.getIdScheda() == idScheda) {
                    scheda = s;
                    break;
                }
            }
        }
        if (scheda == null) {
            response.sendRedirect("home");
            return;
        }
        String fileName = scheda.getNome().replaceAll("\\s+", "_") + "_" + scheda.getIdScheda() + ".pdf";
        String realPath = getServletContext().getRealPath("/files/") + fileName;
        File dir = new File(getServletContext().getRealPath("/files/"));
        if (!dir.exists()) dir.mkdirs();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(realPath));
            document.open();
            document.addTitle("Scheda Allenamento");
            document.add(new Paragraph("Scheda: " + scheda.getNome()));
            document.add(new Paragraph("Tipo: " + scheda.getTipo()));
            document.add(new Paragraph("Giorni: " + scheda.getGiorni()));
            document.add(new Paragraph("Focus: " + scheda.getFocus()));
            document.add(new Paragraph("Livello: " + scheda.getLivello()));
            document.add(new Paragraph("Intensità: " + scheda.getIntensita()));
            document.add(new Paragraph("Note: " + (scheda.getNote() != null ? scheda.getNote() : "")));

            document.add(new Paragraph("\nEsercizi della scheda:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            model.SchedaEsercizioDAO schedaEsercizioDAO = new model.SchedaEsercizioDAO();
            List<model.SchedaEsercizio> listaSE = schedaEsercizioDAO.doRetrieveByIdScheda(scheda.getIdScheda());
            if (listaSE.isEmpty()) {
                document.add(new Paragraph("Nessun esercizio associato."));
            } else {
                model.EsercizioDAO esercizioDAO = new model.EsercizioDAO();
                List<model.Esercizio> tuttiEsercizi = esercizioDAO.doRetrieveAll();
                for (model.SchedaEsercizio se : listaSE) {
                    for (model.Esercizio esercizio : tuttiEsercizi) {
                        if (esercizio.getIdEsercizio() == se.getIdEsercizio()) {
                            document.add(new Paragraph(
                                "- " + esercizio.getNome() + " | Gruppo: " + esercizio.getGruppoMuscolare() +
                                " | Serie: " + esercizio.getSerieSuggerite() +
                                " | Ripetizioni: " + esercizio.getRipetizioni() +
                                " | Carico: " + esercizio.getCaricoSuggerito() + " kg" +
                                " | Recupero: " + esercizio.getRecupero()
                            ));
                        }
                    }
                }
            }
            document.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
        // Salva il file esportato in DB
        model.FileEsportato file = new model.FileEsportato();
        file.setIdScheda(scheda.getIdScheda());
        file.setPercorso("files/" + fileName);
        file.setTipoFile("pdf");
        file.setDataCreazione(new Date());
        new model.FileEsportatoDAO().doSave(file);

        // Messaggio di successo
        request.setAttribute("pdfSuccess", true);
        request.setAttribute("pdfPath", "files/" + fileName);
        request.setAttribute("schedaSalvata", true);
        request.setAttribute("nomeScheda", scheda.getNome());
        request.setAttribute("idSchedaSalvata", scheda.getIdScheda());

        // Se la richiesta arriva da manuale.jsp, mostra Scarica PDF e Home
        if (idSchedaParam != null && !idSchedaParam.isEmpty()) {
            request.setAttribute("schedaManualeSalvata", false);
            request.setAttribute("pdfManualeSuccess", true);
            request.getRequestDispatcher("manuale.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("generate.jsp").forward(request, response);
        }
    }
}
