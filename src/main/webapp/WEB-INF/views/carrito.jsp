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

            <c:choose>
                <c:when test="${empty carrito.detalles}">
                    <div class="carrito-vacio">
                        <div class="carrito-vacio-icono">🛒</div>
                        <p class="carrito-vacio-titulo">Tu carrito esta vacio</p>
                        <p class="carrito-vacio-texto">Aun no has agregado productos. Explora el catalogo y encuentra lo que necesitas.</p>
                        <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">Explorar Productos</a>
                    </div>
                </c:when>
                <c:otherwise>
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
                        </tbody>
                    </table>

                    <p style="text-align:right; font-size:16px; margin-top:14px;">
                        <strong>Total: <fmt:formatNumber value="${carrito.total}" type="currency" currencySymbol="S/ "/></strong>
                    </p>
                    <p style="text-align:right;">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Seguir comprando</a>
                        <a class="btn btn-warning" href="${pageContext.request.contextPath}/carrito?accion=vaciar">Vaciar carrito</a>
                        <a class="btn btn-success" href="${pageContext.request.contextPath}/carrito?accion=finalizar">Finalizar compra</a>
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

        <%@ include file="_footer.jsp" %>
    </body>
</html>