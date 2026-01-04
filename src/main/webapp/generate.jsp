<%@ include file="header.jsp" %>
<div class="container">

  <%-- STATO 1: SCHEDA SALVATA CON SUCCESSO --%>
  <c:if test="${schedaSalvata}">
    <div class="card text-center">
      <h2 style="color:var(--success)">Scheda Salvata!</h2>
      <p>La scheda <strong>${nomeScheda}</strong> è stata aggiunta al tuo profilo.</p>
      <div class="mt-2">
        <c:if test="${pdfSuccess}">
          <a href="${pdfPath}" target="_blank" class="btn btn-primary"><i class="fas fa-download"></i> Scarica PDF</a>
        </c:if>
        <a href="home" class="btn btn-secondary">Torna alla Home</a>
      </div>
    </div>
  </c:if>

  <%-- STATO 2: VISUALIZZA ANTEPRIMA SCHEDA GENERATA --%>
  <c:if test="${not empty eserciziScheda && !schedaSalvata}">
    <h1>${titoloScheda}</h1>
    <div class="card">
      <p><strong>Focus:</strong> ${focus}</p>
      <p><strong>Note del Coach:</strong> ${notaScheda}</p>

      <table class="mt-2">
        <thead>
        <tr>
          <th>Esercizio</th>
          <th>Gruppo</th>
          <th>Serie</th>
          <th>Reps</th>
          <th>Carico (kg)</th>
          <th>Recupero</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ex" items="${eserciziScheda}">
          <tr>
            <td>${ex.nome}</td>
            <td>${ex.gruppoMuscolare}</td>
            <td>${ex.serieSuggerite}</td>
            <td>${ex.ripetizioni}</td>
            <td>${ex.caricoSuggerito}</td>
            <td>${ex.recupero}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>

      <form action="salvaScheda" method="post" class="mt-2">
        <div class="form-group">
          <label for="nomeScheda">Dai un nome alla tua scheda:</label>
          <input type="text" name="nomeScheda" required placeholder="Es. Full Body Lunedì">
        </div>
        <input type="hidden" name="tipoScheda" value="Automatica">
        <input type="hidden" name="focusScheda" value="${focus}">
        <input type="hidden" name="notaScheda" value="${notaScheda}">

        <div style="display:flex; gap:10px;">
          <button type="submit" class="btn btn-primary">SALVA SCHEDA</button>
          <button type="submit" formaction="esportaPdf" class="btn btn-secondary">ESPORTA PDF SOLO</button>
        </div>
      </form>
    </div>
  </c:if>

  <%-- STATO 3: NESSUNA SCHEDA GENERATA (Redirect o Bottone per generare) --%>
  <c:if test="${empty eserciziScheda && !schedaSalvata}">
    <div class="card text-center">
      <h2>Generazione Automatica</h2>
      <p>Genera una scheda basata sulle tue preferenze salvate nel profilo.</p>
      <a href="generate" class="btn btn-primary mt-2">GENERA ORA</a>
    </div>
  </c:if>
</div>
<%@ include file="footer.jsp" %>