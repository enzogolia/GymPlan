<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- IMPORTANTE: Usa questo URI per Tomcat 10/11 --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GymPlan</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<header>
    <div class="logo">
        <a href="${pageContext.request.contextPath}/home">
            <img src="${pageContext.request.contextPath}/images/GymPlanOriginale.png" alt="GymPlan Logo" class="logo-img">
            <span>GYMPLAN</span>
        </a>
    </div>

    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/home">HOME</a></li>

            <%-- Visibile solo se loggato --%>
            <c:if test="${sessionScope.loggedIn}">
                <li><a href="${pageContext.request.contextPath}/manuale">CREA MANUALE</a></li>
                <li><a href="${pageContext.request.contextPath}/generate">GENERA AUTOMATICA</a></li>
                <li><a href="${pageContext.request.contextPath}/report">STORICO</a></li>
            </c:if>
        </ul>
    </nav>

    <div class="user-actions">
        <c:choose>
            <c:when test="${sessionScope.loggedIn}">
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary" title="Vai al profilo">
                    <i class="fas fa-user-circle"></i> ${sessionScope.user.username}
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">
                    <i class="fas fa-sign-out-alt"></i> ESCI
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">ACCEDI</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">REGISTRATI</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<div class="main-content">