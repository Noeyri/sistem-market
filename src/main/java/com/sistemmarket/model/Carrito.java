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
}
