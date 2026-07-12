<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
        <table>
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th>Stock</th>
                    <th>Cantidad</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty productos}">
                        <tr><td colspan="5">No hay productos disponibles.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${productos}">
                            <tr>
                                <td>${p.nombre}</td>
                                <td><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="S/ "/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stock <= 0}">
                                            <span style="color:#dc2626; font-weight:bold;">Sin stock</span>
                                        </c:when>
                                        <c:otherwise>${p.stock}</c:otherwise>
                                    </c:choose>
                                </td>
                                <c:choose>
                                    <c:when test="${p.stock > 0}">
                                        <td style="width:90px;">
                                            <form class="inline" method="post" action="${pageContext.request.contextPath}/carrito">
                                                <input type="hidden" name="productoId" value="${p.id}">
                                                <input type="number" name="cantidad" value="1" min="1" max="${p.stock}" style="width:70px; margin-bottom:0;">
                                        </td>
                                        <td>
                                                <button type="submit" class="btn btn-primary">Agregar</button>
                                            </form>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td colspan="2"></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

<%@ include file="_footer.jsp" %>
</body>
</html>