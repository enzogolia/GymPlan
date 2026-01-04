<%@ include file="header.jsp" %>
<div class="container">
  <h1>Modifica Preferenze</h1>
  <c:if test="${salvato}">
    <div class="alert alert-success">Preferenze aggiornate con successo!</div>
  </c:if>

  <div class="card">
    <form action="modifyDataServlet" method="post">
      <div class="form-group">
        <label>Giorni Disponibili per l'allenamento</label>
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
        <select name="livelloEsperienza">
          <option value="Principiante" ${preferenze.livelloEsperienza == 'Principiante' ? 'selected' : ''}>Principiante</option>
          <option value="Intermedio" ${preferenze.livelloEsperienza == 'Intermedio' ? 'selected' : ''}>Intermedio</option>
          <option value="Esperto" ${preferenze.livelloEsperienza == 'Esperto' ? 'selected' : ''}>Esperto</option>
        </select>
      </div>

      <div class="form-group">
        <label>Gruppi Muscolari Preferiti</label>
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
        <label>Intensità Desiderata (1-6)</label>
        <input type="number" name="intensita" min="1" max="6" value="${preferenze.intensita}">
      </div>

      <div class="form-group">
        <label>Eventuali Infortuni (descrizione)</label>
        <textarea name="infortuni" rows="3">${preferenze.infortuni}</textarea>
      </div>

      <button type="submit" class="btn btn-primary">Salva Modifiche</button>
    </form>
  </div>
</div>
<%@ include file="footer.jsp" %>