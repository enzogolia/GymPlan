package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@WebServlet("/esportaPdf")
public class EsportaPdfServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Controllo Autenticazione
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;
        if (userObj == null || !(userObj instanceof Utente)) {
            response.sendRedirect("login");
            return;
        }
        Utente utente = (Utente) userObj;

        // 2. Recupero Dati Scheda
        String nomeScheda = request.getParameter("nomeScheda");
        String idSchedaParam = request.getParameter("idScheda");
        SchedaAllenamento scheda = null;
        int idScheda = -1;
        SchedaAllenamentoDAO dao = new SchedaAllenamentoDAO();

        // Logica di recupero identica alla tua
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

        // 3. Generazione PDF in Memoria (ByteArrayOutputStream)
        // Invece di salvare su File, usiamo la RAM per poi spedirlo subito
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // --- CONTENUTO DEL PDF ---
            document.addTitle("Scheda Allenamento");

            // Intestazione più carina
            Paragraph titolo = new Paragraph("Scheda: " + scheda.getNome(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titolo.setAlignment(Element.ALIGN_CENTER);
            document.add(titolo);
            document.add(new Paragraph(" ")); // Spazio vuoto

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

                // Usiamo una lista puntata per ordine
                com.itextpdf.text.List pdfList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

                for (model.SchedaEsercizio se : listaSE) {
                    for (model.Esercizio esercizio : tuttiEsercizi) {
                        if (esercizio.getIdEsercizio() == se.getIdEsercizio()) {
                            String riga = esercizio.getNome() +
                                    " | Gruppo: " + esercizio.getGruppoMuscolare() +
                                    " | Serie: " + esercizio.getSerieSuggerite() +
                                    " | Reps: " + esercizio.getRipetizioni() +
                                    " | Carico: " + esercizio.getCaricoSuggerito() + " kg" +
                                    " | Rec: " + esercizio.getRecupero();
                            pdfList.add(new ListItem(riga));
                        }
                    }
                }
                document.add(pdfList);
            }
            document.close();

        } catch (Exception e) {
            throw new ServletException("Errore durante la creazione del PDF", e);
        }

        // 4. Salvataggio traccia nel DB (Opzionale, ma utile per storico)
        // Nota: Il percorso non è più fisico, mettiamo una stringa segnaposto
        String fileName = scheda.getNome().replaceAll("\\s+", "_") + "_" + scheda.getIdScheda() + ".pdf";
        try {
            model.FileEsportato file = new model.FileEsportato();
            file.setIdScheda(scheda.getIdScheda());
            file.setPercorso("DOWNLOAD_DIRETTO");
            file.setTipoFile("pdf");
            file.setDataCreazione(new Date());
            new model.FileEsportatoDAO().doSave(file);
        } catch(Exception e) {
            // Logghiamo l'errore ma non blocchiamo il download utente
            e.printStackTrace();
        }

        // 5. INVIO DEL FILE AL BROWSER (La parte fondamentale)
        response.setContentType("application/pdf");
        // Questo header forza il browser a scaricare il file invece di aprirlo
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());

        // Scriviamo il buffer nel flusso di output della risposta
        try (OutputStream os = response.getOutputStream()) {
            baos.writeTo(os);
            os.flush();
        }

        // IMPORTANTE: Nessun forward o redirect qui! La risposta è stata già inviata (il file).
    }
}