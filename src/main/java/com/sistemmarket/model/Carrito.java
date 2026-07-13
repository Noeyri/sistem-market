package com.sistemmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int id;
    private int usuarioId;
    private String estado; // ACTIVO, FINALIZADO
    private List<DetalleCarrito> detalles = new ArrayList<>();

    public Carrito() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleCarrito> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCarrito> detalles) { this.detalles = detalles; }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCarrito d : detalles) {
            total = total.add(d.getSubtotal());
        }
        return total;
    }
    
    private java.sql.Timestamp fechaCreacion;
    private String usuarioNombre; // solo se usa en la vista de pedidos del ADMIN

    public java.sql.Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.sql.Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    
    private String estadoPedido; // PENDIENTE, PROCESANDO, ENTREGADO

    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }
}
