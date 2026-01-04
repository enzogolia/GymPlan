<%@ include file="header.jsp" %>
<div class="container">
  <h1>Storico Allenamenti</h1>
  <div class="card">
    <table>
      <thead>
      <tr>
        <th>Scheda</th>
        <th>Durata</th>
        <th>Reps Totali</th>
        <th>Carico Medio</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="rep" items="${reportAllenamentoUtente}">
        <tr>
          <td>${rep.nomeScheda}</td>
          <td>${rep.durataMedia}</td> <td>${rep.ripetizioniTotali}</td>
          <td>${rep.pesoMedio} kg</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <c:if test="${empty reportAllenamentoUtente}">
      <p>Non hai ancora completato nessun allenamento.</p>
    </c:if>
  </div>
</div>
<%@ include file="footer.jsp" %>