<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

<header>
    <div class="titulo">Sistem Market</div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <c:if test="${usuario.rol == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/productos">Productos</a>
            <a href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
            <a href="${pageContext.request.contextPath}/pedidos">Pedidos</a>
        </c:if>
        <c:if test="${usuario.rol == 'USUARIO'}">
            <a href="${pageContext.request.contextPath}/catalogo">Productos</a>
            <a href="${pageContext.request.contextPath}/carrito">Carrito</a>
            <a href="${pageContext.request.contextPath}/pedidos">Mis Pedidos</a>
        </c:if>
        <div class="dropdown">
            <span class="dropdown-trigger">${usuario.nombre} <span class="flecha">&#9662;</span></span>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/logout">Cerrar Sesion</a>
            </div>
        </div>
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