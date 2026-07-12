<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Formulario Usuario - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>
        <div class="contenedor">
            <div class="card" style="max-width:500px;">
                <h2>
                    <c:choose>
                        <c:when test="${not empty usuarioEditar}">Editar Usuario</c:when>
                        <c:otherwise>Nuevo Usuario</c:otherwise>
                    </c:choose>
                </h2>

                <c:if test="${not empty error}">
                    <div class="alerta-error">${error}</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/usuarios">
                    <c:if test="${not empty usuarioEditar}">
                        <input type="hidden" name="id" value="${usuarioEditar.id}">
                    </c:if>

                    <label>Username</label>
                    <input type="text" name="username" value="${usuarioEditar.username}" required>

                    <label>Nombre completo</label>
                    <input type="text" name="nombre" value="${usuarioEditar.nombre}" required>

                    <label>Rol</label>
                    <select name="rol">
                        <c:choose>
                            <c:when test="${usuarioEditar.rol == 'ADMIN'}">
                                <option value="ADMIN" selected>ADMIN</option>
                                <option value="USUARIO">USUARIO</option>
                            </c:when>
                            <c:when test="${usuarioEditar.rol == 'USUARIO'}">
                                <option value="ADMIN">ADMIN</option>
                                <option value="USUARIO" selected>USUARIO</option>
                            </c:when>
                            <c:otherwise>
                                <option value="ADMIN">ADMIN</option>
                                <option value="USUARIO">USUARIO</option>
                            </c:otherwise>
                        </c:choose>
                    </select>

                    <label>
                        Contrasena
                        <c:if test="${not empty usuarioEditar}"> (dejar en blanco para no cambiarla)</c:if>
                        </label>
                        <input type="password" name="password" ${empty usuarioEditar ? 'required' : ''}>

                    <button type="submit" class="btn btn-primary" style="border:none; cursor:pointer;">Guardar</button>
                    <a class="btn btn-danger" href="${pageContext.request.contextPath}/usuarios">Cancelar</a>
                </form>
                <%@ include file="_footer.jsp" %>
            </div>
        </div>
    </body>
</html>
