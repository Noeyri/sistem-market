<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<c:set var="breadcrumb">
    <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a> &gt; Pedidos
</c:set>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Pedidos - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>

        <div class="card">
            <h2>
                <c:choose>
                    <c:when test="${usuario.rol == 'ADMIN'}">Pedidos de todos los clientes</c:when>
                    <c:otherwise>Mis Pedidos</c:otherwise>
                </c:choose>
            </h2>

            <c:choose>
                <c:when test="${empty pedidos}">
                    <p>Todavia no hay pedidos registrados.</p>
                </c:when>
                <c:otherwise>
                    <%-- INICIO DEL BUCLE DE PEDIDOS ACTUALIZADO --%>
                    <c:forEach var="pedido" items="${pedidos}">
                        <div class="pedido-card">
                            <div class="pedido-card-header">
                                <div>
                                    <strong>Pedido #${pedido.id}</strong>
                                    <span class="pedido-fecha">
                                        <fmt:formatDate value="${pedido.fechaCreacion}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                    <%-- Muestra el nombre del usuario solo si es ADMIN --%>
                                    <c:if test="${usuario.rol == 'ADMIN'}">
                                        <span class="badge badge-usuario">${pedido.usuarioNombre}</span>
                                    </c:if>
                                </div>

                                <c:choose>
                                    <c:when test="${pedido.estadoPedido == 'PENDIENTE'}">
                                        <span class="badge badge-pendiente">Pendiente</span>
                                    </c:when>
                                    <c:when test="${pedido.estadoPedido == 'PROCESANDO'}">
                                        <span class="badge badge-procesando">Procesando</span>
                                    </c:when>
                                    <c:when test="${pedido.estadoPedido == 'ENTREGADO'}">
                                        <span class="badge badge-entregado">Entregado</span>
                                    </c:when>
                                </c:choose>
                            </div>

                            <table style="margin-top:12px;">
                                <thead>
                                    <tr>
                                        <th>Producto</th>
                                        <th>Precio Unit.</th>
                                        <th>Cantidad</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${pedido.detalles}">
                                        <tr>
                                            <td>${d.productoNombre}</td>
                                            <td><fmt:formatNumber value="${d.precioUnitario}" type="currency" currencySymbol="S/ "/></td>
                                            <td>${d.cantidad}</td>
                                            <td><fmt:formatNumber value="${d.subtotal}" type="currency" currencySymbol="S/ "/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <div class="pedido-card-footer">
                                <%-- Cliente: boton para confirmar su propio pedido pendiente --%>
                                <c:if test="${usuario.rol == 'USUARIO' and pedido.estadoPedido == 'PENDIENTE'}">
                                    <form action="${pageContext.request.contextPath}/ConfirmarCompraServlet" method="POST" class="inline">
                                        <input type="hidden" name="pedidoId" value="${pedido.id}">
                                        <button type="submit" class="btn btn-success">Confirmar Compra</button>
                                    </form>
                                </c:if>

                                <%-- Admin: boton para marcar como entregado cuando esta en PROCESANDO --%>
                                <c:if test="${usuario.rol == 'ADMIN' and pedido.estadoPedido == 'PROCESANDO'}">
                                    <form action="${pageContext.request.contextPath}/ActualizarPedidoServlet" method="POST" class="inline">
                                        <input type="hidden" name="pedidoId" value="${pedido.id}">
                                        <button type="submit" class="btn btn-primary">Marcar como Entregado</button>
                                    </form>
                                </c:if>

                                <strong>Total: <fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="S/ "/></strong>
                            </div>
                        </div>
                    </c:forEach>
                    <%-- FIN DEL BUCLE DE PEDIDOS ACTUALIZADO --%>
                </c:otherwise>
            </c:choose>
        </div>

        <%@ include file="_footer.jsp" %>
    </body>
</html>