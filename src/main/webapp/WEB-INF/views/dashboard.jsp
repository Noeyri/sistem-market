<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="usuario" value="${sessionScope.usuario}" />
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Sistem Market</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <%@ include file="_header.jsp" %>

        <c:if test="${param.error == 'acceso_denegado'}">
            <div class="alerta-error">No tienes permisos para acceder a esa seccion.</div>
        </c:if>

        <c:choose>
            <c:when test="${usuario.rol == 'ADMIN'}">
                <h2 class="dashboard-titulo">Panel de Administrador</h2>

                <!-- Fila 1: KPIs (siempre visibles) -->
                <div class="kpi-grid">
                    <div class="kpi-card">
                        <span class="kpi-label">Productos</span>
                        <span class="kpi-valor">${estadisticas.totalProductos}</span>
                    </div>
                    <div class="kpi-card kpi-alerta">
                        <span class="kpi-label">Stock bajo</span>
                        <span class="kpi-valor">${estadisticas.productosStockBajo}</span>
                    </div>
                    <div class="kpi-card">
                        <span class="kpi-label">Usuarios</span>
                        <span class="kpi-valor">${estadisticas.totalUsuarios}</span>
                    </div>
                    <div class="kpi-card kpi-exito">
                        <span class="kpi-label">Pedidos</span>
                        <span class="kpi-valor">${estadisticas.totalPedidos}</span>
                    </div>
                </div>

                <p style="margin: 18px 0;">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/productos">Gestionar Productos</a>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios">Gestionar Usuarios</a>
                </p>

                <!-- ===== Secciones intercambiables (CSS puro) ===== -->
                <div class="tabs">
                    <input type="radio" name="admin-tabs" id="tab-resumen" class="tab-input" checked>
                    <input type="radio" name="admin-tabs" id="tab-usuarios" class="tab-input">

                    <div class="tabs-nav">
                        <label for="tab-resumen" class="tab-label">Resumen</label>
                        <label for="tab-usuarios" class="tab-label">Usuarios</label>
                    </div>

                    <!-- ===== PANEL: RESUMEN (Pedidos recientes + Top productos) ===== -->
                    <div class="tab-panel" id="panel-resumen">

                        <div class="card">
                            <h2>Pedidos recientes</h2>
                            <c:choose>
                                <c:when test="${empty estadisticas.pedidosRecientes}">
                                    <p>Todavia no hay pedidos registrados.</p>
                                </c:when>
                                <c:otherwise>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Pedido</th><th>Cliente</th><th>Fecha</th><th>Estado</th><th>Total</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="pedido" items="${estadisticas.pedidosRecientes}">
                                                <tr>
                                                    <td>#${pedido.id}</td>
                                                    <td>${pedido.usuarioNombre}</td>
                                                    <td><fmt:formatDate value="${pedido.fechaCreacion}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${pedido.estadoPedido == 'PENDIENTE'}"><span class="badge badge-pendiente">Pendiente</span></c:when>
                                                            <c:when test="${pedido.estadoPedido == 'PROCESANDO'}"><span class="badge badge-procesando">Procesando</span></c:when>
                                                            <c:when test="${pedido.estadoPedido == 'ENTREGADO'}"><span class="badge badge-entregado">Entregado</span></c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td><fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="S/ "/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <p style="text-align:right; margin-top:12px;">
                                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/pedidos">Ver todos los pedidos</a>
                                    </p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="card">
                            <h2>Top productos mas vendidos</h2>

                            <c:choose>
                                <c:when test="${empty estadisticas.topProductos}">
                                    <p>Aun no hay ventas registradas.</p>
                                </c:when>
                                <c:otherwise>
                                    <!-- Mini-toggle: Tabla / Barras / Circular -->
                                    <div class="tabs mini-tabs">
                                        <input type="radio" name="top-vista" id="top-tabla" class="tab-input" checked>
                                        <input type="radio" name="top-vista" id="top-barras" class="tab-input">
                                        <input type="radio" name="top-vista" id="top-circular" class="tab-input">

                                        <div class="tabs-nav mini-tabs-nav">
                                            <label for="top-tabla" class="tab-label">Tabla</label>
                                            <label for="top-barras" class="tab-label">Barras</label>
                                            <label for="top-circular" class="tab-label">Circular</label>
                                        </div>

                                        <div class="tab-panel" id="panel-top-tabla">
                                            <table>
                                                <thead><tr><th>Producto</th><th>Unidades vendidas</th></tr></thead>
                                                <tbody>
                                                    <c:forEach var="tp" items="${estadisticas.topProductos}">
                                                        <tr><td>${tp.nombre}</td><td>${tp.cantidadVendida}</td></tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                        <div class="tab-panel" id="panel-top-barras">
                                            <div class="grafico-columnas">
                                                <c:forEach var="tp" items="${estadisticas.topProductos}">
                                                    <div class="columna-item">
                                                        <span class="columna-valor">${tp.cantidadVendida}</span>
                                                        <div class="columna-pista">
                                                            <div class="columna-barra" style="height:${tp.porcentajeBarra}%; background:${tp.colorHex};"></div>
                                                        </div>
                                                        <span class="columna-etiqueta">${tp.nombre}</span>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>

                                        <div class="tab-panel" id="panel-top-circular">
                                            <div class="grafico-circular-contenedor">
                                                <div class="grafico-dona" style="background: conic-gradient(${estadisticas.graficoTopProductosCss});">
                                                    <div class="grafico-dona-centro"></div>
                                                </div>
                                                <ul class="leyenda">
                                                    <c:forEach var="tp" items="${estadisticas.topProductos}">
                                                        <li>
                                                            <span class="leyenda-color" style="background:${tp.colorHex};"></span>
                                                            ${tp.nombre} <strong>(${tp.porcentajeTotal}%)</strong>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- ===== PANEL: USUARIOS ===== -->
                    <div class="tab-panel" id="panel-usuarios">
                        <div class="kpi-grid kpi-grid-usuario" style="margin-bottom:20px;">
                            <div class="kpi-card">
                                <span class="kpi-label">Administradores</span>
                                <span class="kpi-valor">${estadisticas.totalAdmins}</span>
                            </div>
                            <div class="kpi-card kpi-exito">
                                <span class="kpi-label">Clientes</span>
                                <span class="kpi-valor">${estadisticas.totalClientes}</span>
                            </div>
                            <div class="kpi-card">
                                <span class="kpi-label">Total usuarios</span>
                                <span class="kpi-valor">${estadisticas.totalUsuarios}</span>
                            </div>
                        </div>

                        <div class="card">
                            <h2>Listado de usuarios</h2>
                            <table>
                                <thead><tr><th>Username</th><th>Nombre</th><th>Rol</th></tr></thead>
                                <tbody>
                                    <c:forEach var="u" items="${estadisticas.usuarios}">
                                        <tr>
                                            <td>${u.username}</td>
                                            <td>${u.nombre}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${u.rol == 'ADMIN'}"><span class="badge badge-admin">ADMIN</span></c:when>
                                                    <c:otherwise><span class="badge badge-usuario">USUARIO</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <p style="text-align:right; margin-top:12px;">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios">Gestionar Usuarios</a>
                            </p>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <h2 class="dashboard-titulo">Panel de Usuario</h2>

                <div class="kpi-grid kpi-grid-usuario">
                    <div class="kpi-card">
                        <span class="kpi-label">Mis pedidos</span>
                        <span class="kpi-valor">${estadisticas.totalPedidos}</span>
                    </div>
                    <div class="kpi-card kpi-exito">
                        <span class="kpi-label">Total gastado</span>
                        <span class="kpi-valor"><fmt:formatNumber value="${estadisticas.totalGastado}" type="currency" currencySymbol="S/ "/></span>
                    </div>
                    <div class="kpi-card">
                        <span class="kpi-label">Estado ultimo pedido</span>
                        <span class="kpi-valor kpi-valor-chico">
                            <c:choose>
                                <c:when test="${estadisticas.estadoUltimoPedido == 'PENDIENTE'}"><span class="badge badge-pendiente">Pendiente</span></c:when>
                                <c:when test="${estadisticas.estadoUltimoPedido == 'PROCESANDO'}"><span class="badge badge-procesando">Procesando</span></c:when>
                                <c:when test="${estadisticas.estadoUltimoPedido == 'ENTREGADO'}"><span class="badge badge-entregado">Entregado</span></c:when>
                                <c:otherwise><span style="color:var(--texto-suave); font-size:13px;">Sin pedidos</span></c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>

                <p style="margin: 18px 0;">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/catalogo">Ver Productos</a>
                    <a class="btn btn-success" href="${pageContext.request.contextPath}/carrito">Ir al Carrito</a>
                </p>

                <div class="card">
                    <h2>Mis ultimos pedidos</h2>
                    <c:choose>
                        <c:when test="${empty estadisticas.ultimosPedidos}">
                            <p>Todavia no has realizado ningun pedido. <a href="${pageContext.request.contextPath}/catalogo">Explora los productos</a>.</p>
                        </c:when>
                        <c:otherwise>
                            <table>
                                <thead><tr><th>Pedido</th><th>Fecha</th><th>Estado</th><th>Total</th></tr></thead>
                                <tbody>
                                    <c:forEach var="pedido" items="${estadisticas.ultimosPedidos}">
                                        <tr>
                                            <td>#${pedido.id}</td>
                                            <td><fmt:formatDate value="${pedido.fechaCreacion}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${pedido.estadoPedido == 'PENDIENTE'}"><span class="badge badge-pendiente">Pendiente</span></c:when>
                                                    <c:when test="${pedido.estadoPedido == 'PROCESANDO'}"><span class="badge badge-procesando">Procesando</span></c:when>
                                                    <c:when test="${pedido.estadoPedido == 'ENTREGADO'}"><span class="badge badge-entregado">Entregado</span></c:when>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatNumber value="${pedido.total}" type="currency" currencySymbol="S/ "/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <p style="text-align:right; margin-top:12px;">
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/pedidos">Ver todos mis pedidos</a>
                            </p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- ===== Estadisticas del mes actual ===== -->
                <div class="card">
                    <h2>Mi actividad este mes</h2>
                    <div class="grafico-columnas grafico-columnas-usuario">
                        <div class="columna-item">
                            <span class="columna-valor">${estadisticas.pedidosMesActual}</span>
                            <div class="columna-pista">
                                <div class="columna-barra" style="height:${estadisticas.porcentajePedidosMes}%; background:var(--primario);"></div>
                            </div>
                            <span class="columna-etiqueta">Pedidos este mes</span>
                        </div>
                        <div class="columna-item">
                            <span class="columna-valor"><fmt:formatNumber value="${estadisticas.gastoMesActual}" type="currency" currencySymbol="S/ "/></span>
                            <div class="columna-pista">
                                <div class="columna-barra" style="height:${estadisticas.porcentajeGastoMes}%; background:var(--exito);"></div>
                            </div>
                            <span class="columna-etiqueta">Gasto este mes</span>
                        </div>
                    </div>
                    <p class="ayuda" style="margin-top:10px;">Comparado contra tu historial total de pedidos y gasto.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <%@ include file="_footer.jsp" %>
    </body>
</html>