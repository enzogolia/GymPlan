<%@ include file="header.jsp" %>
<div class="container">
  <h1>Il mio Profilo</h1>
  <div class="card">
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem;">
      <div>
        <h3>Dati Personali</h3>
        <p><strong>Username:</strong> ${username}</p>
        <p><strong>Nome:</strong> ${nome}</p>
        <p><strong>Cognome:</strong> ${cognome}</p>
        <p><strong>Email:</strong> ${email}</p>
      </div>
      <div>
        <h3>Dati Fisici</h3>
        <p><strong>Età:</strong> ${eta} anni</p>
        <p><strong>Peso:</strong> ${peso} kg</p>
      </div>
    </div>
    <div class="mt-2 text-center">
      <a href="modifyDataServlet" class="btn btn-secondary">Modifica Preferenze e Dati</a>
    </div>
  </div>
</div>
<%@ include file="footer.jsp" %>