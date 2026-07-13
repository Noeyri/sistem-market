package com.sistemmarket.dto;

import com.sistemmarket.model.Carrito;

import java.math.BigDecimal;
import java.util.List;

public class EstadisticasUsuarioDTO {
    private int totalPedidos;
    private BigDecimal totalGastado;
    private String estadoUltimoPedido;
    private List<Carrito> ultimosPedidos;

    // Estadisticas del mes actual
    private int pedidosMesActual;
    private BigDecimal gastoMesActual;
    private int porcentajePedidosMes; // pedidosMesActual / totalPedidos * 100
    private int porcentajeGastoMes;   // gastoMesActual / totalGastado * 100

    public int getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(int totalPedidos) { this.totalPedidos = totalPedidos; }

    public BigDecimal getTotalGastado() { return totalGastado; }
    public void setTotalGastado(BigDecimal totalGastado) { this.totalGastado = totalGastado; }

    public String getEstadoUltimoPedido() { return estadoUltimoPedido; }
    public void setEstadoUltimoPedido(String estadoUltimoPedido) { this.estadoUltimoPedido = estadoUltimoPedido; }

    public List<Carrito> getUltimosPedidos() { return ultimosPedidos; }
    public void setUltimosPedidos(List<Carrito> ultimosPedidos) { this.ultimosPedidos = ultimosPedidos; }

    public int getPedidosMesActual() { return pedidosMesActual; }
    public void setPedidosMesActual(int pedidosMesActual) { this.pedidosMesActual = pedidosMesActual; }

    public BigDecimal getGastoMesActual() { return gastoMesActual; }
    public void setGastoMesActual(BigDecimal gastoMesActual) { this.gastoMesActual = gastoMesActual; }

    public int getPorcentajePedidosMes() { return porcentajePedidosMes; }
    public void setPorcentajePedidosMes(int porcentajePedidosMes) { this.porcentajePedidosMes = porcentajePedidosMes; }

    public int getPorcentajeGastoMes() { return porcentajeGastoMes; }
    public void setPorcentajeGastoMes(int porcentajeGastoMes) { this.porcentajeGastoMes = porcentajeGastoMes; }
}