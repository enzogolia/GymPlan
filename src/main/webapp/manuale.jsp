<%@ include file="header.jsp" %>
<div class="container">
  <h1>Crea Scheda Manuale</h1>

  <c:if test="${schedaManualeSalvata}">
    <div class="alert alert-success">Scheda creata con successo!</div>
    <c:if test="${pdfManualeSuccess}">
      <a href="esportaPdf?idScheda=${idSchedaSalvata}" class="btn btn-primary">Scarica PDF</a>
    </c:if>
  </c:if>

  <c:if test="${not empty errore}">
    <div class="alert alert-error">${errore}</div>
  </c:if>

  <form action="manuale" method="post">
    <div class="card">
      <h3>Dettagli Scheda</h3>
      <div class="form-group">
        <label>Titolo Scheda</label>
        <input type="text" name="titoloScheda" required>
      </div>
      <div class="form-group" style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap:20px;">
        <div>
          <label>Giorni a settimana</label>
          <input type="number" name="giorniScheda" min="1" max="7" value="3">
        </div>
        <div>
          <label>Livello (1-3)</label>
          <input type="number" name="livelloScheda" min="1" max="3" value="1">
        </div>
        <div>
          <label>Intensità (1-6)</label>
          <input type="number" name="intensitaScheda" min="1" max="6" value="3">
        </div>
      </div>
      <div class="form-group">
        <label>Note</label>
        <textarea name="notaScheda" rows="2"></textarea>
      </div>
    </div>

    <div class="card">
      <h3>Seleziona Esercizi</h3>
      <div style="height: 300px; overflow-y: scroll; border: 1px solid #444; padding: 10px;">
        <table style="font-size:0.9rem;">
          <thead>
          <tr>
            <th>Sel.</th>
            <th>Nome</th>
            <th>Gruppo</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="ex" items="${esercizi}">
            <tr>
              <td><input type="checkbox" name="eserciziSelezionati" value="${ex.idEsercizio}"></td>
              <td>${ex.nome}</td>
              <td>${ex.gruppoMuscolare}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <button type="submit" class="btn btn-primary mt-2">SALVA SCHEDA</button>
    </div>
  </form>
</div>
<%@ include file="footer.jsp" %>
