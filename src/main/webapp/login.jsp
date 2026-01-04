<%@ include file="header.jsp" %>
<div class="container" style="display:flex; justify-content:center; align-items:center;">
  <div class="card" style="width: 100%; max-width: 400px;">
    <h2 class="text-center">Accedi</h2>
    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="loginUpdateServlet" method="post">
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
      </div>
      <button type="submit" class="btn btn-primary" style="width:100%">LOGIN</button>
    </form>
    <p class="text-center mt-2">Non hai un account? <a href="register" style="color:var(--accent-orange)">Registrati</a></p>
  </div>
</div>
<%@ include file="footer.jsp" %>