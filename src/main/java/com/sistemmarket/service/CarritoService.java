package com.sistemmarket.service;

import com.sistemmarket.dao.CarritoDAO;
import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.dao.ProductoDAO;
import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.Producto;

public class CarritoService {

    private final CarritoDAO carritoDAO = DAOFactory.getCarritoDAO();
    private final ProductoDAO productoDAO = DAOFactory.getProductoDAO();
    
    public Carrito obtenerCarrito(int usuarioId) {
        return carritoDAO.obtenerOCrearCarritoActivo(usuarioId);
    }

    public void agregarProducto(int usuarioId, int productoId, int cantidad) {
        Producto producto = productoDAO.buscarPorId(productoId);
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        Carrito carrito = carritoDAO.obtenerOCrearCarritoActivo(usuarioId);
        carritoDAO.agregarProducto(carrito.getId(), producto.getId(), producto.getPrecio(), cantidad);
    }

    public void eliminarDetalle(int detalleId) {
        carritoDAO.eliminarDetalle(detalleId);
    }

    public void vaciarCarrito(int usuarioId) {
        Carrito carrito = carritoDAO.obtenerOCrearCarritoActivo(usuarioId);
        carritoDAO.vaciarCarrito(carrito.getId());
    }

    public void finalizarCompra(int usuarioId) {
        Carrito carrito = carritoDAO.obtenerOCrearCarritoActivo(usuarioId);
        carritoDAO.finalizarCarrito(carrito.getId());
    }
}
