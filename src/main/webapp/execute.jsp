<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- Inclusione dell'header --%>
<%@ include file="header.jsp" %>

<%
  // Controllo di sicurezza: se non c'è stato o flag finished, torna al profilo
  if (session.getAttribute("workoutState") == null && request.getAttribute("finished") == null) {
    response.sendRedirect("profile.jsp");
    return;
  }
%>

<div class="container">

  <c:choose>
    <%-- ==================================================================
         SCENARIO 1: ALLENAMENTO COMPLETATO
         ================================================================== --%>
    <c:when test="${finished}">
      <div class="text-center">
        <h1 style="color: var(--success); margin-bottom: 2rem;">
          <i class="fas fa-trophy"></i> ALLENAMENTO COMPLETATO!
        </h1>

        <div class="card" style="max-width: 600px; margin: 0 auto;">
          <h3 style="border-bottom: 1px solid #333; padding-bottom: 10px; margin-bottom: 20px;">
            Report Sessione
          </h3>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; text-align: left;">
            <div>
              <p><strong style="color:var(--accent-orange)">Reps Totali:</strong> ${report.ripetizioniTotali}</p>
              <p><strong style="color:var(--accent-orange)">Sessioni:</strong> ${report.sessioniTotali}</p>
            </div>
            <div>
              <p><strong style="color:var(--accent-orange)">Carico Medio:</strong> <fmt:formatNumber value="${report.pesoMedio}" maxFractionDigits="1"/> kg</p>
              <p><strong style="color:var(--accent-orange)">Durata:</strong> ${report.durataMedia}</p>
            </div>
          </div>
        </div>

        <div class="mt-2">
          <a href="home" class="btn btn-primary">TORNA ALLA HOME</a>
          <a href="report" class="btn btn-secondary">VAI ALLO STORICO</a>
        </div>
      </div>
    </c:when>

    <%-- ==================================================================
         SCENARIO 2: ESECUZIONE IN CORSO
         ================================================================== --%>
    <c:otherwise>
      <c:set var="state" value="${sessionScope.workoutState}" />
      <c:set var="currEx" value="${state.currentExercise}" />

      <%-- Calcolo se siamo all'ultimo step (ultima serie dell'ultimo esercizio) --%>
      <c:set var="isLastStep" value="${state.currentExerciseIndex == state.esercizi.size() - 1 && state.currentSetIndex == currEx.serieSuggerite}" />

      <h1 class="text-center" style="margin-bottom: 20px;">
          ${state.scheda.nome} <span style="font-size: 0.6em; color: var(--text-secondary);">(In corso)</span>
      </h1>

      <div class="grid-container" style="grid-template-columns: 1fr 1fr; gap: 20px; align-items: stretch;">

          <%-- COLONNA SINISTRA: CONTROLLI --%>
          <%-- justify-content: space-between distribuisce gli elementi per riempire l'altezza fissa --%>
        <div class="card equal-height-card" style="display: flex; flex-direction: column; justify-content: space-between;">

          <div>
            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
              <div>
                <h2 style="margin-bottom: 5px; color: var(--accent-orange);">${currEx.nome}</h2>
                <p style="font-size: 0.9rem; color: var(--text-secondary);">Gruppo: ${currEx.gruppoMuscolare}</p>
              </div>
              <div style="background: #333; padding: 5px 10px; border-radius: 4px; font-weight: bold;">
                Serie: ${state.currentSetIndex} / ${currEx.serieSuggerite}
              </div>
            </div>

            <div class="alert" style="background-color: rgba(255, 152, 0, 0.1); border: 1px solid var(--accent-orange); margin: 1rem 0;">
              <strong>Target:</strong> ${currEx.ripetizioni} Reps &nbsp;|&nbsp;
              <strong>Recupero:</strong> <span id="recuperoTime">${currEx.recupero}</span>
            </div>
          </div>

          <div style="text-align: center; padding: 1rem; background: #1a1a1a; border-radius: 8px; margin: 10px 0;">
            <div id="timerDisplay" style="font-size: 3rem; font-weight: bold; font-family: monospace; color: var(--text-primary);">00:00</div>
            <div style="margin-top: 10px; display: flex; gap: 10px; justify-content: center;">
              <button type="button" class="btn btn-secondary" onclick="startTimer()"><i class="fas fa-play"></i> Start</button>
              <button type="button" class="btn btn-secondary" onclick="stopTimer()"><i class="fas fa-pause"></i> Stop</button>
              <button type="button" class="btn btn-secondary" onclick="resetTimer()"><i class="fas fa-redo"></i> Reset</button>
            </div>
          </div>

          <div>
            <div style="display: flex; gap: 20px; margin-bottom: 1rem;">
              <div style="flex: 1;">
                <label for="weight">Carico (Kg)</label>
                <input type="number" id="weight" name="weight" value="${currEx.caricoSuggerito}" step="0.5" style="font-size: 1.2rem;">
              </div>
              <div style="flex: 1;">
                <label for="reps">Ripetizioni</label>
                <input type="number" id="reps" name="reps" value="${currEx.ripetizioni}" style="font-size: 1.2rem;">
              </div>
            </div>

            <div style="display: flex; gap: 15px;">
              <button type="button" class="btn btn-primary" id="btnSave" onclick="saveAndNext()" style="flex: 2; padding: 1rem; font-size: 1.1rem;">
                <i class="fas fa-check"></i>
                <c:choose>
                  <c:when test="${isLastStep}">SALVA E TERMINA</c:when>
                  <c:otherwise>SALVA E PROSSIMO</c:otherwise>
                </c:choose>
              </button>

              <button type="button" class="btn btn-danger" id="btnSkip" onclick="skipAndNext()" style="flex: 1; background-color: #d32f2f;">
                <i class="fas fa-forward"></i> SALTA
              </button>
            </div>
          </div>

            <%-- Form nascosto per finire l'allenamento --%>
          <form action="${pageContext.request.contextPath}/executeScheda" method="post" id="finishForm">
            <input type="hidden" name="action" value="finish">
          </form>

        </div>

          <%-- COLONNA DESTRA: IMMAGINE --%>
          <%-- Nota: Usa le classi 'equal-height-card' e 'execution-card-container' --%>
        <div class="card equal-height-card execution-card-container">
          <h3 class="text-center" style="margin-bottom: 1rem;">Esecuzione</h3>

          <div class="img-wrapper">
            <img src="${pageContext.request.contextPath}/images/${currEx.immagine}"
                 alt="${currEx.nome}"
                 class="execution-img"
                 onerror="this.src='https://placehold.co/400x300?text=No+Image';">
          </div>
        </div>

      </div>
    </c:otherwise>
  </c:choose>

</div>

<%-- JAVASCRIPT --%>
<script>
  // Variabile per sapere se è l'ultimo step
  const isLastStep = ${isLastStep != null ? isLastStep : false};

  let timerInterval;
  let seconds = 0;
  let targetSeconds = 0;

  // --- PARSING TEMPO RECUPERO ---
  const recuperoElement = document.getElementById('recuperoTime');
  if (recuperoElement) {
    const timeText = recuperoElement.innerText.trim();
    const parts = timeText.split(':');
    if (parts.length === 3) {
      targetSeconds = (+parts[0]) * 3600 + (+parts[1]) * 60 + (+parts[2]);
    } else if (parts.length === 2) {
      targetSeconds = (+parts[0]) * 60 + (+parts[1]);
    } else {
      targetSeconds = 90; // Default 1m 30s
    }
  }

  // --- LOGICA TIMER ---
  function updateTimerDisplay() {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    const display = document.getElementById('timerDisplay');
    if(display) display.innerText = m + ':' + s;
  }

  function startTimer() {
    if (timerInterval) return;
    document.getElementById('timerDisplay').style.color = "var(--accent-orange)";
    timerInterval = setInterval(() => {
      seconds++;
      updateTimerDisplay();
      if (targetSeconds > 0 && seconds >= targetSeconds) {
        stopTimer();
        alert("Tempo di recupero terminato!");
        document.getElementById('timerDisplay').style.color = "var(--success)";
      }
    }, 1000);
  }

  function stopTimer() {
    clearInterval(timerInterval);
    timerInterval = null;
    const disp = document.getElementById('timerDisplay');
    if(disp) disp.style.color = "var(--text-primary)";
  }

  function resetTimer() {
    stopTimer();
    seconds = 0;
    updateTimerDisplay();
  }

  // --- LOGICA SALVATAGGIO ---
  function saveAndNext() {
    const reps = document.getElementById('reps').value;
    const weight = document.getElementById('weight').value;

    if(!reps || !weight) {
      alert("Inserisci ripetizioni e carico.");
      return;
    }

    const btnSave = document.getElementById('btnSave');
    const originalText = btnSave.innerHTML;

    // Feedback visivo
    btnSave.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Attendi...';
    btnSave.disabled = true;

    // Chiamata AJAX
    fetch('${pageContext.request.contextPath}/executeScheda', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: 'action=saveSet&reps=' + reps + '&weight=' + weight
    }).then(response => {
      if (response.ok) {
        handleAutomaticNext();
      } else {
        alert("Errore nel salvataggio server.");
        btnSave.innerHTML = originalText;
        btnSave.disabled = false;
      }
    }).catch(error => {
      console.error('Error:', error);
      alert("Errore di connessione.");
      btnSave.innerHTML = originalText;
      btnSave.disabled = false;
    });
  }

  function skipAndNext() {
    if(confirm("Vuoi saltare questa serie?")) {
      const btnSkip = document.getElementById('btnSkip');
      btnSkip.disabled = true;

      fetch('${pageContext.request.contextPath}/executeScheda', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=skipSet'
      }).then(response => {
        if (response.ok) {
          handleAutomaticNext();
        } else {
          alert("Errore server.");
          btnSkip.disabled = false;
        }
      });
    }
  }

  function handleAutomaticNext() {
    if (isLastStep) {
      document.getElementById('finishForm').submit();
    } else {
      fetch('${pageContext.request.contextPath}/executeScheda', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=next'
      }).then(response => {
        if (response.ok) {
          window.location.reload();
        }
      });
    }
  }
</script>

<%@ include file="footer.jsp" %>