<%@ include file="header.jsp" %>
<div class="container">

  <%-- CASO 1: SESSIONE FINITA (REPORT) --%>
  <c:if test="${finished}">
    <div class="card text-center">
      <h1 style="color:var(--success)">Allenamento Completato! <i class="fas fa-check-circle"></i></h1>
      <div style="display:grid; grid-template-columns: 1fr 1fr; gap:20px; text-align:left; margin-top:2rem;">
        <div>
          <p><strong>Durata:</strong> ${report.durataMedia}</p>
          <p><strong>Volume Totale (Reps):</strong> ${report.ripetizioniTotali}</p>
        </div>
        <div>
          <p><strong>Carico Medio:</strong> ${report.pesoMedio} kg</p>
          <p><strong>Sessioni Totali Scheda:</strong> ${report.sessioniTotali}</p>
        </div>
      </div>
      <a href="home" class="btn btn-primary mt-2">TORNA ALLA HOME</a>
    </div>
  </c:if>

  <%-- CASO 2: SESSIONE IN CORSO --%>
  <c:if test="${!finished && sessionScope.workoutState != null}">
    <c:set var="state" value="${sessionScope.workoutState}" />
    <c:set var="currEx" value="${state.currentExercise}" />

    <div class="card">
      <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2>${currEx.nome}</h2>
        <span style="background:var(--accent-orange); color:black; padding:2px 8px; border-radius:4px; font-weight:bold;">
                    Set ${state.currentSetIndex} / ${currEx.serieSuggerite}
                </span>
      </div>
      <p style="color:var(--text-secondary)">Gruppo: ${currEx.gruppoMuscolare} | Recupero: ${currEx.recupero}</p>

      <div class="alert" style="background:#333; margin-top:1rem;">
        <strong>Obiettivo:</strong> ${currEx.ripetizioni} Ripetizioni con ${currEx.caricoSuggerito} Kg
      </div>

      <form action="executeScheda" method="post" style="margin-top:2rem;">
        <div style="display:flex; gap:20px; margin-bottom:2rem;">
          <div style="flex:1;">
            <label>Ripetizioni Fatte</label>
            <input type="number" name="reps" value="${currEx.ripetizioni}" required>
          </div>
          <div style="flex:1;">
            <label>Carico Usato (Kg)</label>
            <input type="number" name="weight" value="${currEx.caricoSuggerito}" step="0.5" required>
          </div>
        </div>

        <div style="display:flex; gap:10px; flex-wrap:wrap;">
          <button type="submit" name="action" value="saveSet" class="btn btn-primary">SALVA SERIE</button>
          <button type="submit" name="action" value="skipSet" class="btn btn-secondary" style="border-color:#555; color:#aaa;">SALTA</button>
        </div>

        <hr style="margin: 2rem 0; border-color:#333;">

        <div style="display:flex; justify-content:space-between;">
          <button type="submit" name="action" value="finish" class="btn btn-danger">TERMINA ALLENAMENTO</button>
          <button type="submit" name="action" value="next" class="btn" style="background:white; color:black;">PROSSIMO ESERCIZIO &gt;</button>
        </div>
      </form>
    </div>
  </c:if>

  <%-- CASO DI ERRORE O NESSUNO STATO --%>
  <c:if test="${!finished && sessionScope.workoutState == null}">
    <div class="alert alert-error">Nessuna sessione attiva. Torna alla home.</div>
    <a href="home" class="btn btn-primary">Home</a>
  </c:if>

</div>
<%@ include file="footer.jsp" %>