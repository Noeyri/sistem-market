<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

<header>
    <div class="titulo">Sistem Market</div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <c:if test="${usuario.rol == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/productos">Productos</a>
            <a href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
        </c:if>
        <c:if test="${usuario.rol == 'USUARIO'}">
            <a href="${pageContext.request.contextPath}/catalogo">Productos</a>
            <a href="${pageContext.request.contextPath}/carrito">Carrito</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/logout">Salir (${usuario.nombre})</a>
    </nav>
</header>

<c:if test="${not empty breadcrumb}">
    <div class="breadcrumb">${breadcrumb}</div>
</c:if>

<div class="contenedor">
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alerta-ok">
            <span>${sessionScope.flashSuccess}</span>
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alerta-error">
            <span>${sessionScope.flashError}</span>
        </div>
        <c:remove var="flashError" scope="session"/>
    </c:if>
</div>