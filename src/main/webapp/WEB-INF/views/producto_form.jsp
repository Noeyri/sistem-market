<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<c:set var="breadcrumb">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a> &gt;
    <a href="${pageContext.request.contextPath}/productos">Productos</a> &gt;
    <c:choose><c:when test="${not empty producto}">Editar</c:when><c:otherwise>Nuevo</c:otherwise></c:choose>
</c:set>
<!DOCTYPE html>
<html lang="es">
    <head><meta charset="UTF-8"><title>Formulario Producto - Sistem Market</title></head>
    <body>
        <%@ include file="_header.jsp" %>

        <div class="card" style="max-width:500px;">
            <h2><c:choose><c:when test="${not empty producto}">Editar Producto</c:when><c:otherwise>Nuevo Producto</c:otherwise></c:choose></h2>

                        <form method="post" action="${pageContext.request.contextPath}/productos">
                <c:if test="${not empty producto}"><input type="hidden" name="id" value="${producto.id}"></c:if>

                    <label class="campo-requerido">Nombre</label>
                    <input type="text" name="nombre" value="${producto.nombre}" maxlength="150" required>

                <label>Descripcion</label>
                <textarea name="descripcion" rows="3" maxlength="500">${producto.descripcion}</textarea>

                <label class="campo-requerido">Precio</label>
                <input type="number" step="0.01" min="0.01" name="precio" value="${producto.precio}" required>

                <label class="campo-requerido">Stock</label>
                <input type="number" min="0" name="stock" value="${producto.stock}" required>
                <div class="ayuda">* Campos obligatorios</div>

                <button type="submit" class="btn btn-primary">Guardar</button>
                <a class="btn btn-danger" href="${pageContext.request.contextPath}/productos">Cancelar</a>
            </form>
        </div>

        <%@ include file="_footer.jsp" %>
    </body>
</html>