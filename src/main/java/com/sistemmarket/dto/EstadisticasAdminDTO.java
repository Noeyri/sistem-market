package com.sistemmarket.dto;

import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.Usuario;

import java.util.List;

public class EstadisticasAdminDTO {
    private int totalProductos;
    private int productosStockBajo;
    private int totalUsuarios;
    private int totalPedidos;
    private List<Carrito> pedidosRecientes;
    private List<ProductoVendidoDTO> topProductos;
    private String graficoTopProductosCss; // string listo para conic-gradient()

    // Seccion "Usuarios"
    private int totalAdmins;
    private int totalClientes;
    private List<Usuario> usuarios;

    public int getTotalProductos() { return totalProductos; }
    public void setTotalProductos(int totalProductos) { this.totalProductos = totalProductos; }

    public int getProductosStockBajo() { return productosStockBajo; }
    public void setProductosStockBajo(int productosStockBajo) { this.productosStockBajo = productosStockBajo; }

    public int getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(int totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public int getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(int totalPedidos) { this.totalPedidos = totalPedidos; }

    public List<Carrito> getPedidosRecientes() { return pedidosRecientes; }
    public void setPedidosRecientes(List<Carrito> pedidosRecientes) { this.pedidosRecientes = pedidosRecientes; }

    public List<ProductoVendidoDTO> getTopProductos() { return topProductos; }
    public void setTopProductos(List<ProductoVendidoDTO> topProductos) { this.topProductos = topProductos; }

    public String getGraficoTopProductosCss() { return graficoTopProductosCss; }
    public void setGraficoTopProductosCss(String graficoTopProductosCss) { this.graficoTopProductosCss = graficoTopProductosCss; }

    public int getTotalAdmins() { return totalAdmins; }
    public void setTotalAdmins(int totalAdmins) { this.totalAdmins = totalAdmins; }

    public int getTotalClientes() { return totalClientes; }
    public void setTotalClientes(int totalClientes) { this.totalClientes = totalClientes; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}