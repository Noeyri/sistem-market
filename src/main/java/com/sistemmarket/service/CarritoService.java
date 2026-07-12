package com.sistemmarket.service;

import com.sistemmarket.dao.CarritoDAO;
import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.dao.ProductoDAO;
import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.DetalleCarrito;
import com.sistemmarket.model.Producto;
import com.sistemmarket.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

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
        if (cantidad > producto.getStock()) {
            throw new IllegalArgumentException("No hay stock suficiente de " + producto.getNombre());
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

    /**
     * Finaliza la compra: descuenta el stock de cada producto y marca
     * el carrito como FINALIZADO. Todo dentro de una sola transaccion:
     * si algun producto no tiene stock suficiente, se revierte todo.
     */
    public void finalizarCompra(int usuarioId) {
        Carrito carrito = carritoDAO.obtenerOCrearCarritoActivo(usuarioId);

        if (carrito.getDetalles().isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio");
        }

        try (Connection con = ConexionBD.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (DetalleCarrito detalle : carrito.getDetalles()) {
                    boolean descontado = productoDAO.descontarStock(
                            con, detalle.getProductoId(), detalle.getCantidad());
                    if (!descontado) {
                        con.rollback();
                        throw new IllegalStateException(
                                "No hay stock suficiente de " + detalle.getProductoNombre());
                    }
                }
                carritoDAO.finalizarCarrito(con, carrito.getId());
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Error al finalizar la compra", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexion al finalizar la compra", e);
        }
    }
}