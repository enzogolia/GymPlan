<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<div class="container">
  <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
    <h1>Crea Scheda Manuale</h1>
    <a href="home" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Torna alla Home</a>
  </div>

  <%-- Messaggi di Feedback --%>
  <c:if test="${schedaManualeSalvata}">
    <div class="alert alert-success">
      <i class="fas fa-check-circle"></i> Scheda <strong>${titoloScheda}</strong> creata con successo!
    </div>
    <div style="margin-bottom: 2rem;">
      <c:if test="${pdfManualeSuccess}">
        <form action="esportaPdf" method="post" target="_blank" style="display:inline;">
          <input type="hidden" name="idScheda" value="${idSchedaSalvata}">
          <button type="submit" class="btn btn-primary"><i class="fas fa-download"></i> Scarica PDF Ora</button>
        </form>
      </c:if>
      <a href="manuale" class="btn btn-secondary">Crea un'altra scheda</a>
    </div>
  </c:if>

  <c:if test="${not empty errore}">
    <div class="alert alert-error"><i class="fas fa-exclamation-triangle"></i> ${errore}</div>
  </c:if>

  <form action="manuale" method="post" id="manualForm">

    <div class="card">
      <h3 style="border-bottom: 1px solid #333; padding-bottom: 10px; margin-bottom: 20px;">
        <i class="fas fa-info-circle" style="color:var(--accent-orange)"></i> Dettagli Scheda
      </h3>

      <div class="form-group">
        <label>Titolo Scheda</label>
        <input type="text" name="titoloScheda" required placeholder="Es. Ipertrofia Gambe" value="${titoloScheda}">
      </div>

      <div class="form-group" style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:20px;">
        <div>
          <label>Giorni a settimana</label>
          <select name="giorniScheda">
            <option value="1">1 Giorno</option>
            <option value="2">2 Giorni</option>
            <option value="3" selected>3 Giorni</option>
            <option value="4">4 Giorni</option>
            <option value="5">5 Giorni</option>
            <option value="6">6 Giorni</option>
            <option value="7">7 Giorni</option>
          </select>
        </div>
        <div>
          <label>Livello Scheda</label>
          <select name="livelloScheda" id="livelloSelect" onchange="filterTable()">
            <option value="1">Principiante</option>
            <option value="2">Intermedio</option>
            <option value="3">Esperto</option>
          </select>
          <small style="color:#666; font-size:0.8rem;">Determina gli esercizi disponibili</small>
        </div>
        <div>
          <label>Intensità (1-6)</label>
          <input type="number" name="intensitaScheda" min="1" max="6" value="3">
        </div>
      </div>

      <div class="form-group">
        <label>Note Opzionali</label>
        <textarea name="notaScheda" rows="2" placeholder="Inserisci note per l'esecuzione..."></textarea>
      </div>
    </div>

    <div class="card">
      <h3 style="border-bottom: 1px solid #333; padding-bottom: 10px; margin-bottom: 20px;">
        <i class="fas fa-dumbbell" style="color:var(--accent-orange)"></i> Seleziona Esercizi
      </h3>

      <div style="position: relative;">
        <i class="fas fa-search" style="position: absolute; right: 15px; top: 15px; color: #888;"></i>
        <input type="text" id="searchInput" class="search-input" onkeyup="filterTable()" placeholder="Cerca per nome esercizio o gruppo muscolare...">
      </div>

      <div class="table-scroll-container">
        <table id="exercisesTable">
          <thead>
          <tr>
            <th style="width: 60px; text-align: center;">Sel.</th>
            <th>Nome Esercizio</th>
            <th>Gruppo</th>
            <th>Difficoltà</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="ex" items="${esercizi}">
            <tr class="exercise-row" data-livello="${ex.livello}">
              <td style="text-align: center; vertical-align: middle;">
                <label class="custom-checkbox-container">
                  <input type="checkbox" name="eserciziSelezionati" value="${ex.idEsercizio}">
                  <span class="checkmark"></span>
                </label>
              </td>
              <td style="vertical-align: middle; font-weight: 500;">${ex.nome}</td>
              <td style="vertical-align: middle; color: var(--accent-orange);">${ex.gruppoMuscolare}</td>
              <td style="vertical-align: middle;">
                <c:choose>
                  <c:when test="${ex.livello == 'principiante'}"><span class="badge badge-1">Principiante</span></c:when>
                  <c:when test="${ex.livello == 'intermedio'}"><span class="badge badge-2">Intermedio</span></c:when>
                  <c:when test="${ex.livello == 'esperto'}"><span class="badge badge-3">Esperto</span></c:when>
                  <c:otherwise><span class="badge badge-1">${ex.livello}</span></c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

      <div style="margin-top: 10px; display:flex; justify-content:space-between; color: #888; font-size: 0.9rem;">
                <span id="hiddenMessage" style="color: var(--danger); display:none;">
                    <i class="fas fa-filter"></i> Alcuni esercizi sono nascosti perché di livello superiore alla scheda.
                </span>
        <span><span id="counterRows">${esercizi.size()}</span> esercizi visibili</span>
      </div>

      <button type="submit" class="btn btn-primary mt-2" style="width: 100%; padding: 1rem; font-size: 1.2rem;">
        <i class="fas fa-save"></i> SALVA SCHEDA
      </button>
    </div>
  </form>
</div>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    filterTable();
  });

  // Mappa le stringhe del DB in numeri per la logica "minore o uguale"
  function convertLevelToInt(levelStr) {
    if (!levelStr) return 1; // Default
    levelStr = levelStr.toLowerCase().trim();

    if (levelStr === 'esperto') return 3;
    if (levelStr === 'intermedio') return 2;
    // Principiante o altro
    return 1;
  }

  function filterTable() {
    // 1. INPUT DI RICERCA TESTUALE
    var input = document.getElementById("searchInput");
    var filterText = input.value.toUpperCase();

    // 2. LIVELLO SELEZIONATO DALL'UTENTE (Select value: 1, 2, 3)
    var levelSelect = document.getElementById("livelloSelect");
    var maxLevel = levelSelect ? parseInt(levelSelect.value) : 3;

    var table = document.getElementById("exercisesTable");
    var tr = table.getElementsByTagName("tr");
    var visibleCount = 0;
    var hiddenByLevel = false;

    // 3. CICLO SU TUTTE LE RIGHE
    for (var i = 1; i < tr.length; i++) {
      var tdNome = tr[i].getElementsByTagName("td")[1];
      var tdGruppo = tr[i].getElementsByTagName("td")[2];

      // Leggi la stringa dall'HTML (es. "Intermedio")
      var exLevelStr = tr[i].getAttribute("data-livello");
      // Convertila in numero (es. 2)
      var exLevel = convertLevelToInt(exLevelStr);

      if (tdNome && tdGruppo) {
        var txtValueNome = tdNome.textContent || tdNome.innerText;
        var txtValueGruppo = tdGruppo.textContent || tdGruppo.innerText;

        // A. Filtro Testuale
        var matchesText = (txtValueNome.toUpperCase().indexOf(filterText) > -1) ||
                (txtValueGruppo.toUpperCase().indexOf(filterText) > -1);

        // B. Filtro Livello (Numero Esercizio <= Numero Selezionato)
        var matchesLevel = exLevel <= maxLevel;

        if (matchesText && matchesLevel) {
          tr[i].style.display = "";
          visibleCount++;
        } else {
          tr[i].style.display = "none";

          // Se il testo corrispondeva ma il livello no, attiviamo l'avviso
          if (matchesText && !matchesLevel) {
            hiddenByLevel = true;
          }
        }
      }
    }

    document.getElementById("counterRows").innerText = visibleCount;

    var msgBox = document.getElementById("hiddenMessage");
    if(hiddenByLevel) {
      msgBox.style.display = "inline";
    } else {
      msgBox.style.display = "none";
    }
  }
</script>

<%@ include file="footer.jsp" %>