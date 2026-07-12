<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Usuarios - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>
        <div class="contenedor">
            <div class="card">
                <h2>Gestion de Usuarios</h2>
                <p><a class="btn btn-success" href="${pageContext.request.contextPath}/usuarios?accion=nuevo">+ Nuevo Usuario</a></p>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Nombre</th>
                            <th>Rol</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty usuarios}">
                                <tr><td colspan="5">No hay usuarios registrados.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="u" items="${usuarios}">
                                    <tr>
                                        <td>${u.id}</td>
                                        <td>${u.username}</td>
                                        <td>${u.nombre}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${u.rol == 'ADMIN'}">
                                                    <span class="badge badge-admin">ADMIN</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-usuario">USUARIO</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="acciones">
                                            <a class="btn btn-warning" href="${pageContext.request.contextPath}/usuarios?accion=editar&id=${u.id}">Editar</a>
                                            <c:if test="${u.id != usuario.id}">
                                                <a class="btn btn-danger" href="${pageContext.request.contextPath}/usuarios?accion=eliminar&id=${u.id}"
                                                   onclick="return confirm('Eliminar usuario ${u.username}?');">Eliminar</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <%@ include file="_footer.jsp" %>
        </div>
    </body>
</html>
