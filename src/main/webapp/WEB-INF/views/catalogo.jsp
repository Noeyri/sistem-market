<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<c:set var="breadcrumb">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a> &gt; Productos
</c:set>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Productos - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>

        <div class="card">
            <h2>Productos disponibles</h2>

            <!-- Barra de busqueda -->
            <form method="get" action="${pageContext.request.contextPath}/catalogo" class="barra-busqueda">
                <input type="text" name="busqueda" placeholder="Buscar producto..." value="${param.busqueda}">
                <button type="submit" class="btn btn-primary">Buscar</button>
                <c:if test="${not empty param.busqueda}">
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-warning">Limpiar</a>
                </c:if>
            </form>

            <!-- Filtro por categorias (estetico) -->
            <div class="filtro-categorias">
                <a href="${pageContext.request.contextPath}/catalogo"
                   class="chip ${empty categoriaActiva ? 'chip-activo' : ''}">Todos</a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=abarrotes"
                   class="chip ${categoriaActiva == 'abarrotes' ? 'chip-activo' : ''}">Abarrotes</a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=lacteos"
                   class="chip ${categoriaActiva == 'lacteos' ? 'chip-activo' : ''}">Lacteos</a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=limpieza"
                   class="chip ${categoriaActiva == 'limpieza' ? 'chip-activo' : ''}">Limpieza</a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=bebidas"
                   class="chip ${categoriaActiva == 'bebidas' ? 'chip-activo' : ''}">Bebidas</a>
            </div>

            <!-- Grid de tarjetas -->
            <c:choose>
                <c:when test="${empty productos}">
                    <p>No hay productos disponibles.</p>
                </c:when>
                <c:otherwise>
                    <div class="grid-productos">
                        <c:forEach var="p" items="${productos}">
                            <c:if test="${empty param.busqueda or fn:contains(fn:toLowerCase(p.nombre), fn:toLowerCase(param.busqueda))}">
                                <div class="producto-card">
                                    <div class="producto-card-cuerpo">
                                        <h3>${p.nombre}</h3>
                                        <p class="producto-precio"><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="S/ "/></p>

                                        <p class="producto-stock">
                                            <c:choose>
                                                <c:when test="${p.stock <= 0}">
                                                    <span class="badge badge-agotado">Sin stock</span>
                                                </c:when>
                                                <c:when test="${p.stock <= 10}">
                                                    <span class="badge badge-bajo">Quedan ${p.stock}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    Stock: ${p.stock}
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>

                                    <c:if test="${p.stock > 0}">
                                        <form method="post" action="${pageContext.request.contextPath}/carrito" class="producto-card-form">
                                            <input type="hidden" name="productoId" value="${p.id}">
                                            <input type="number" name="cantidad" value="1" min="1" max="${p.stock}">
                                            <button type="submit" class="btn btn-primary">Agregar</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${p.stock <= 0}">
                                        <button class="btn btn-deshabilitado" disabled>No disponible</button>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <%@ include file="_footer.jsp" %>
    </body>
</html>