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
    </head>
    <body>
        <%@ include file="_header.jsp" %>

        <div class="card">
            <h2>Gestion de Productos</h2>
            <p><a class="btn btn-success" href="${pageContext.request.contextPath}/productos?accion=nuevo">+ Nuevo Producto</a></p>

            <table>
                <thead>
                    <tr><th>ID</th><th>Nombre</th><th>Descripcion</th><th>Precio</th><th>Stock</th><th>Acciones</th></tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty productos}">
                            <tr><td colspan="6">No hay productos registrados.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="p" items="${productos}">
                                <tr>
                                    <td>${p.id}</td>
                                    <td>${p.nombre}</td>
                                    <td>${p.descripcion}</td>
                                    <td><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="S/ "/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.stock <= 0}"><span style="color:#c0392b; font-weight:bold;">Sin stock</span></c:when>
                                            <c:otherwise>${p.stock}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="acciones">
                                        <a class="btn btn-warning" href="${pageContext.request.contextPath}/productos?accion=editar&id=${p.id}">Editar</a>
                                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/productos?accion=eliminar&id=${p.id}"
                                           onclick="return confirm('Eliminar producto ${p.nombre}?');">Eliminar</a>
                                    </td>
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