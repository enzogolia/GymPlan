<%@ include file="header.jsp" %>
<div class="container">
  <h1>Modifica Preferenze</h1>

  <%-- Messaggio di successo dal Server --%>
  <c:if test="${salvato}">
    <div class="alert alert-success">Preferenze aggiornate con successo!</div>
  </c:if>

  <%-- Messaggio di errore dal Client (JavaScript) --%>
  <div id="clientError" class="alert alert-error" style="display:none;"></div>

  <div class="card">
    <%-- Aggiunto id="prefForm" per gestirlo via JS --%>
    <form id="prefForm" action="modifyDataServlet" method="post">

      <div class="form-group">
        <label>Giorni Disponibili per l'allenamento <span style="color:red">*</span></label>
        <div style="display:flex; gap:15px; flex-wrap:wrap;">
          <label><input type="checkbox" name="giorniDisponibili" value="Lun" ${preferenze.giorniDisponibili.contains('Lun') ? 'checked' : ''}> Lun</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Mar" ${preferenze.giorniDisponibili.contains('Mar') ? 'checked' : ''}> Mar</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Mer" ${preferenze.giorniDisponibili.contains('Mer') ? 'checked' : ''}> Mer</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Gio" ${preferenze.giorniDisponibili.contains('Gio') ? 'checked' : ''}> Gio</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Ven" ${preferenze.giorniDisponibili.contains('Ven') ? 'checked' : ''}> Ven</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Sab" ${preferenze.giorniDisponibili.contains('Sab') ? 'checked' : ''}> Sab</label>
          <label><input type="checkbox" name="giorniDisponibili" value="Dom" ${preferenze.giorniDisponibili.contains('Dom') ? 'checked' : ''}> Dom</label>
        </div>
      </div>

      <div class="form-group">
        <label>Livello Esperienza</label>
        <select name="livelloEsperienza" required>
          <option value="Principiante" ${preferenze.livelloEsperienza == 'Principiante' ? 'selected' : ''}>Principiante</option>
          <option value="Intermedio" ${preferenze.livelloEsperienza == 'Intermedio' ? 'selected' : ''}>Intermedio</option>
          <option value="Esperto" ${preferenze.livelloEsperienza == 'Esperto' ? 'selected' : ''}>Esperto</option>
        </select>
      </div>

      <div class="form-group">
        <label>Gruppi Muscolari Preferiti <span style="color:red">*</span></label>
        <div style="display:flex; gap:15px; flex-wrap:wrap;">
          <c:forEach var="gruppo" items="${gruppiMuscolariList}">
            <label>
              <input type="checkbox" name="gruppiMuscolari" value="${gruppo}"
                ${preferenze.gruppiMuscolari != null && preferenze.gruppiMuscolari.contains(gruppo) ? 'checked' : ''}>
                ${gruppo}
            </label>
          </c:forEach>
        </div>
      </div>

      <div class="form-group">
        <label>Intensità Desiderata (1-6) <span style="color:red">*</span></label>
        <input type="number" name="intensita" min="1" max="6" value="${preferenze.intensita}" required>
      </div>

      <div class="form-group">
        <label>Eventuali Infortuni (descrizione)</label>
        <textarea name="infortuni" rows="3">${preferenze.infortuni}</textarea>
      </div>

      <button type="submit" class="btn btn-primary">Salva Modifiche</button>
    </form>
  </div>
</div>

<script>
  document.getElementById('prefForm').addEventListener('submit', function(event) {
    let valid = true;
    let errors = [];

    // 1. Controllo Giorni (Checkbox)
    const giorniChecked = document.querySelectorAll('input[name="giorniDisponibili"]:checked').length;
    if (giorniChecked === 0) {
      errors.push("Seleziona almeno un giorno per l'allenamento.");
      valid = false;
    }

    // 2. Controllo Gruppi Muscolari (Checkbox)
    const gruppiChecked = document.querySelectorAll('input[name="gruppiMuscolari"]:checked').length;
    if (gruppiChecked === 0) {
      errors.push("Seleziona almeno un gruppo muscolare preferito.");
      valid = false;
    }

    // 3. Controllo Intensità (anche se l'input type=number aiuta, meglio essere sicuri)
    const intensitaInput = document.querySelector('input[name="intensita"]');
    if (!intensitaInput.value || intensitaInput.value < 1 || intensitaInput.value > 6) {
      errors.push("L'intensità deve essere un numero tra 1 e 6.");
      valid = false;
    }

    // Se ci sono errori, blocca l'invio e mostra il messaggio
    if (!valid) {
      event.preventDefault(); // Blocca il submit
      const errorDiv = document.getElementById('clientError');
      errorDiv.innerHTML = errors.join("<br>"); // Mostra lista errori
      errorDiv.style.display = 'block'; // Rende visibile il div
      window.scrollTo(0, 0); // Scorre in alto per far vedere l'errore
    }
  });
</script>

<%@ include file="footer.jsp" %>