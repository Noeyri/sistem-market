<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<c:set var="breadcrumb">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a> &gt; Carrito
</c:set>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Carrito - Sistem Market</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="_header.jsp" %>

    <div class="card">
        <h2>Mi Carrito</h2>
        <table>
            <thead>
                <tr>
                    <th>Producto</th>
                    <th>Precio Unit.</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty carrito.detalles}">
                        <tr><td colspan="5">
                            Tu carrito esta vacio.
                            <a href="${pageContext.request.contextPath}/catalogo">Ver productos</a>
                        </td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="d" items="${carrito.detalles}">
                            <tr>
                                <td>${d.productoNombre}</td>
                                <td><fmt:formatNumber value="${d.precioUnitario}" type="currency" currencySymbol="S/ "/></td>
                                <td>${d.cantidad}</td>
                                <td><fmt:formatNumber value="${d.subtotal}" type="currency" currencySymbol="S/ "/></td>
                                <td>
                                    <a class="btn btn-danger" href="${pageContext.request.contextPath}/carrito?accion=eliminar&id=${d.id}">Quitar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <c:if test="${not empty carrito.detalles}">
            <p style="text-align:right; font-size:16px; margin-top:14px;">
                <strong>Total: <fmt:formatNumber value="${carrito.total}" type="currency" currencySymbol="S/ "/></strong>
            </p>
            <p style="text-align:right;">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Seguir comprando</a>
                <a class="btn btn-warning" href="${pageContext.request.contextPath}/carrito?accion=vaciar">Vaciar carrito</a>
                <a class="btn btn-success" href="${pageContext.request.contextPath}/carrito?accion=finalizar">Finalizar compra</a>
            </p>
        </c:if>
    </div>

<%@ include file="_footer.jsp" %>
</body>
</html>