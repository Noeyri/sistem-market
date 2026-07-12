<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>
        <div class="contenedor">

            <c:if test="${param.error == 'acceso_denegado'}">
                <div class="alerta-error">No tienes permisos para acceder a esa seccion.</div>
            </c:if>

            <c:choose>
                <c:when test="${usuario.rol == 'ADMIN'}">
                    <div class="card">
                        <h2>Panel de Administrador</h2>
                        <p>Bienvenido, <strong>${usuario.nombre}</strong> <span class="badge badge-admin">ADMIN</span></p>
                        <p>Total de productos registrados: <strong>${totalProductos}</strong></p>
                        <p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/productos">Gestionar Productos</a>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios">Gestionar Usuarios</a>
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card">
                        <h2>Panel de Usuario</h2>
                        <p>Bienvenido, <strong>${usuario.nombre}</strong> <span class="badge badge-usuario">USUARIO</span></p>
                        <p>Desde aqui puedes explorar los productos y gestionar tu carrito de compras.</p>
                        <p>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Ver Productos</a>
                            <a class="btn btn-success" href="${pageContext.request.contextPath}/carrito">Ir al Carrito</a>
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
                
            <%@ include file="_footer.jsp" %>    
        </div>
    </body>
</html>
