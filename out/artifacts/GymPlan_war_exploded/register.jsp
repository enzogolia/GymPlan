<%@ include file="header.jsp" %>
<div class="container" style="display:flex; justify-content:center; align-items:center;">
  <div class="card" style="width: 100%; max-width: 400px;">
    <h2 class="text-center">Registrati</h2>
    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="registerUpdateServlet" method="post">
      <div class="form-group">
        <label for="username">Username (max 12 caratteri)</label>
        <input type="text" id="username" name="username" required pattern="^[a-zA-Z0-9_]{1,12}$">
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-group">
        <label for="password">Password (Min 8 char, 1 numero, 1 simbolo)</label>
        <input type="password" id="password" name="password" required>
      </div>
      <button type="submit" class="btn btn-primary" style="width:100%">REGISTRATI</button>
    </form>
    <p class="text-center mt-2">Hai già un account? <a href="login" style="color:var(--accent-orange)">Accedi</a></p>
  </div>
</div>
<%@ include file="footer.jsp" %>