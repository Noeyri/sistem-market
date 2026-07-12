package com.sistemmarket.model;

import java.math.BigDecimal;

public class DetalleCarrito {
    private int id;
    private int carritoId;
    private int productoId;
    private String productoNombre;
    private BigDecimal precioUnitario;
    private int cantidad;

    public DetalleCarrito() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCarritoId() { return carritoId; }
    public void setCarritoId(int carritoId) { this.carritoId = carritoId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
