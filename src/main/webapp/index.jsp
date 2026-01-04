<%-- Importa Header staticamente --%>
<%@ include file="header.jsp" %>

<div class="container">
    <c:if test="${not empty errorDB}">
        <div class="alert alert-error">${errorDB}</div>
    </c:if>

    <c:choose>
        <%-- ==========================================
             VISTA UTENTE LOGGATO (DASHBOARD)
             ========================================== --%>
        <c:when test="${sessionScope.loggedIn}">
            <div class="welcome-section">
                <h1>Bentornato, <span>${sessionScope.user.nome}</span></h1>
                <p>Pronto per il prossimo allenamento?</p>
            </div>

            <div class="mt-2">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                    <h2>Le tue Schede</h2>
                    <div>
                        <a href="generate" class="btn btn-primary">+ Genera Automatica</a>
                        <a href="manuale" class="btn btn-secondary">+ Crea Manuale</a>
                    </div>
                </div>

                <div class="grid-container">
                    <c:forEach var="scheda" items="${schedeAllenamentoUtente}">
                        <div class="card">
                            <h3>${scheda.nome}</h3>
                            <p style="color:var(--text-secondary); font-size:0.9rem;">
                                <strong>Tipo:</strong> ${scheda.tipo} <br>
                                <strong>Focus:</strong> ${scheda.focus} <br>
                                <strong>Livello:</strong> ${scheda.livello == 1 ? 'Principiante' : (scheda.livello == 2 ? 'Intermedio' : 'Esperto')}
                            </p>
                            <div style="margin-top:1.5rem; display:flex; gap:10px; flex-wrap:wrap;">
                                <form action="executeScheda" method="post">
                                    <input type="hidden" name="action" value="start">
                                    <input type="hidden" name="idScheda" value="${scheda.idScheda}">
                                    <button type="submit" class="btn btn-primary">AVVIA</button>
                                </form>
                                <form action="esportaPdf" method="post" target="_blank">
                                    <input type="hidden" name="idScheda" value="${scheda.idScheda}">
                                    <button type="submit" class="btn btn-secondary"><i class="fas fa-file-pdf"></i> PDF</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty schedeAllenamentoUtente}">
                        <div class="card text-center" style="grid-column: 1 / -1;">
                            <p>Non hai ancora nessuna scheda. Creane una ora!</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:when>

        <%-- ==========================================
             VISTA OSPITE (BANNER + INFO)
             ========================================== --%>
        <c:otherwise>
            <div class="text-center" style="margin-bottom: 3rem;">
                <img src="${pageContext.request.contextPath}/images/banner.jpg"
                     alt="GymPlan Banner"
                     style="max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.5);">

                <div style="margin-top: 2rem;">
                    <a href="register" class="btn btn-primary" style="font-size: 1.2rem; padding: 1rem 2rem;">INIZIA IL TUO PERCORSO</a>
                </div>
            </div>

            <div class="grid-container">

                <div class="card text-center">
                    <i class="fas fa-building fa-3x" style="color:var(--accent-orange); margin-bottom:1rem;"></i>
                    <h3>La Nostra Filosofia</h3>
                    <p style="color:var(--text-secondary);">
                        GymPlan digitalizza l'esperienza della palestra reale.
                        Un ambiente professionale dove l'ingegneria del software incontra il fitness
                        per ottimizzare ogni tuo movimento.
                    </p>
                </div>

                <div class="card text-center">
                    <i class="fas fa-clipboard-list fa-3x" style="color:var(--accent-orange); margin-bottom:1rem;"></i>
                    <h3>Schede Personalizzate</h3>
                    <p style="color:var(--text-secondary);">
                        Crea schede manualmente o lascia fare al nostro algoritmo intelligente.
                        Basato sui tuoi dati fisici, obiettivi e livello di esperienza
                        per risultati garantiti.
                    </p>
                </div>

                <div class="card text-center">
                    <i class="fas fa-stopwatch fa-3x" style="color:var(--accent-orange); margin-bottom:1rem;"></i>
                    <h3>Tracking Live</h3>
                    <p style="color:var(--text-secondary);">
                        Segui i tuoi allenamenti in tempo reale, tieni traccia dei carichi
                        e visualizza i report dei progressi direttamente dalla tua dashboard personale.
                    </p>
                </div>

            </div>
        </c:otherwise>
    </c:choose>
</div>

<%-- Importa Footer staticamente --%>
<%@ include file="footer.jsp" %>