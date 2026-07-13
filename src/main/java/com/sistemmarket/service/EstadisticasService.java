package com.sistemmarket.service;

import com.sistemmarket.dao.CarritoDAO;
import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.dao.ProductoDAO;
import com.sistemmarket.dao.UsuarioDAO;
import com.sistemmarket.dto.EstadisticasAdminDTO;
import com.sistemmarket.dto.EstadisticasUsuarioDTO;
import com.sistemmarket.dto.ProductoVendidoDTO;
import com.sistemmarket.model.Carrito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class EstadisticasService {

    private static final int STOCK_BAJO_UMBRAL = 10;
    private static final int LIMITE_RECIENTES = 5;
    private static final String[] PALETA = {"#4f46e5", "#0ea5e9", "#16a34a", "#f97316", "#ec4899", "#facc15"};

    private final ProductoDAO productoDAO = DAOFactory.getProductoDAO();
    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
    private final CarritoDAO carritoDAO = DAOFactory.getCarritoDAO();

    public EstadisticasAdminDTO obtenerEstadisticasAdmin() {
        EstadisticasAdminDTO dto = new EstadisticasAdminDTO();
        dto.setTotalProductos(productoDAO.contarTotal());
        dto.setProductosStockBajo(productoDAO.contarStockBajo(STOCK_BAJO_UMBRAL));
        dto.setTotalUsuarios(usuarioDAO.contarTotal());
        dto.setTotalPedidos(carritoDAO.contarFinalizados());
        dto.setPedidosRecientes(carritoDAO.listarRecientes(LIMITE_RECIENTES));

        List<ProductoVendidoDTO> top = carritoDAO.listarTopProductosVendidos(LIMITE_RECIENTES);
        asignarColoresYPorcentajes(top);
        dto.setTopProductos(top);
        dto.setGraficoTopProductosCss(construirGradienteConico(top));

        dto.setTotalAdmins(usuarioDAO.contarPorRol("ADMIN"));
        dto.setTotalClientes(usuarioDAO.contarPorRol("USUARIO"));
        dto.setUsuarios(usuarioDAO.listarTodos());

        return dto;
    }

    public EstadisticasUsuarioDTO obtenerEstadisticasUsuario(int usuarioId) {
        EstadisticasUsuarioDTO dto = new EstadisticasUsuarioDTO();
        List<Carrito> ultimos = carritoDAO.listarUltimosPedidosUsuario(usuarioId, LIMITE_RECIENTES);

        int totalPedidos = carritoDAO.contarPorUsuario(usuarioId);
        BigDecimal totalGastado = carritoDAO.calcularTotalGastadoUsuario(usuarioId);
        int pedidosMes = carritoDAO.contarPorUsuarioMesActual(usuarioId);
        BigDecimal gastoMes = carritoDAO.calcularGastoUsuarioMesActual(usuarioId);

        dto.setUltimosPedidos(ultimos);
        dto.setTotalPedidos(totalPedidos);
        dto.setTotalGastado(totalGastado);
        dto.setEstadoUltimoPedido(ultimos.isEmpty() ? null : ultimos.get(0).getEstadoPedido());
        dto.setPedidosMesActual(pedidosMes);
        dto.setGastoMesActual(gastoMes);
        dto.setPorcentajePedidosMes(calcularPorcentaje(pedidosMes, totalPedidos));
        dto.setPorcentajeGastoMes(calcularPorcentaje(gastoMes, totalGastado));

        return dto;
    }

    // ===== Helpers =====

    private void asignarColoresYPorcentajes(List<ProductoVendidoDTO> productos) {
        if (productos.isEmpty()) return;
        int max = productos.stream().mapToInt(ProductoVendidoDTO::getCantidadVendida).max().orElse(1);
        int total = productos.stream().mapToInt(ProductoVendidoDTO::getCantidadVendida).sum();
        for (int i = 0; i < productos.size(); i++) {
            ProductoVendidoDTO p = productos.get(i);
            p.setColorHex(PALETA[i % PALETA.length]);
            p.setPorcentajeBarra(max == 0 ? 0 : (int) Math.round((p.getCantidadVendida() * 100.0) / max));
            p.setPorcentajeTotal(total == 0 ? 0 : (int) Math.round((p.getCantidadVendida() * 100.0) / total));
        }
    }

    // Arma un string listo para usar dentro de conic-gradient(...) en el CSS.
    private String construirGradienteConico(List<ProductoVendidoDTO> productos) {
        int total = productos.stream().mapToInt(ProductoVendidoDTO::getCantidadVendida).sum();
        if (total == 0) return "#e2e8f0 0deg 360deg";

        StringBuilder sb = new StringBuilder();
        double acumulado = 0;
        for (int i = 0; i < productos.size(); i++) {
            ProductoVendidoDTO p = productos.get(i);
            double porcion = (p.getCantidadVendida() * 360.0) / total;
            double inicio = acumulado;
            double fin = acumulado + porcion;
            sb.append(p.getColorHex())
              .append(" ").append(String.format(java.util.Locale.US, "%.1fdeg", inicio))
              .append(" ").append(String.format(java.util.Locale.US, "%.1fdeg", fin));
            if (i < productos.size() - 1) sb.append(", ");
            acumulado = fin;
        }
        return sb.toString();
    }

    private int calcularPorcentaje(int parte, int totalEntero) {
        if (totalEntero == 0) return 0;
        return (int) Math.round((parte * 100.0) / totalEntero);
    }

    private int calcularPorcentaje(BigDecimal parte, BigDecimal totalDecimal) {
        if (totalDecimal == null || totalDecimal.compareTo(BigDecimal.ZERO) == 0) return 0;
        return parte.multiply(BigDecimal.valueOf(100))
                .divide(totalDecimal, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}