package com.sistemmarket.dao;

import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.DetalleCarrito;
import com.sistemmarket.util.ConexionBD;

import java.sql.*;

public class CarritoDAO {

    /* Obtiene el carrito ACTIVO del usuario, o lo crea si no existe. */
    public Carrito obtenerOCrearCarritoActivo(int usuarioId) {
        String sqlBuscar = "SELECT * FROM carrito WHERE usuario_id = ? AND estado = 'ACTIVO'";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Carrito c = new Carrito();
                    c.setId(rs.getInt("id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setEstado(rs.getString("estado"));
                    c.setDetalles(listarDetalles(con, c.getId()));
                    return c;
                }
            }
            // No existe: crear uno nuevo
            String sqlInsert = "INSERT INTO carrito (usuario_id, estado) VALUES (?, 'ACTIVO')";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setInt(1, usuarioId);
                psInsert.executeUpdate();
                try (ResultSet keys = psInsert.getGeneratedKeys()) {
                    if (keys.next()) {
                        Carrito c = new Carrito();
                        c.setId(keys.getInt(1));
                        c.setUsuarioId(usuarioId);
                        c.setEstado("ACTIVO");
                        return c;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener/crear carrito", e);
        }
        throw new RuntimeException("No se pudo obtener ni crear el carrito");
    }

    private java.util.List<DetalleCarrito> listarDetalles(Connection con, int carritoId) throws SQLException {
        java.util.List<DetalleCarrito> lista = new java.util.ArrayList<>();
        String sql = "SELECT dc.*, p.nombre AS producto_nombre FROM detalle_carrito dc " +
                "JOIN productos p ON dc.producto_id = p.id WHERE dc.carrito_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carritoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleCarrito d = new DetalleCarrito();
                    d.setId(rs.getInt("id"));
                    d.setCarritoId(rs.getInt("carrito_id"));
                    d.setProductoId(rs.getInt("producto_id"));
                    d.setProductoNombre(rs.getString("producto_nombre"));
                    d.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    d.setCantidad(rs.getInt("cantidad"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public void agregarProducto(int carritoId, int productoId, java.math.BigDecimal precioUnitario, int cantidad) {
        String sqlBuscar = "SELECT * FROM detalle_carrito WHERE carrito_id = ? AND producto_id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setInt(1, carritoId);
            ps.setInt(2, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int nuevaCantidad = rs.getInt("cantidad") + cantidad;
                    String sqlUpdate = "UPDATE detalle_carrito SET cantidad = ? WHERE id = ?";
                    try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                        psUpdate.setInt(1, nuevaCantidad);
                        psUpdate.setInt(2, rs.getInt("id"));
                        psUpdate.executeUpdate();
                    }
                    return;
                }
            }
            String sqlInsert = "INSERT INTO detalle_carrito (carrito_id, producto_id, precio_unitario, cantidad) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, carritoId);
                psInsert.setInt(2, productoId);
                psInsert.setBigDecimal(3, precioUnitario);
                psInsert.setInt(4, cantidad);
                psInsert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar producto al carrito", e);
        }
    }

    public void eliminarDetalle(int detalleId) {
        String sql = "DELETE FROM detalle_carrito WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar detalle del carrito", e);
        }
    }

    public void vaciarCarrito(int carritoId) {
        String sql = "DELETE FROM detalle_carrito WHERE carrito_id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carritoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al vaciar el carrito", e);
        }
    }

    public void finalizarCarrito(int carritoId) {
        String sql = "UPDATE carrito SET estado = 'FINALIZADO' WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carritoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar el carrito", e);
        }
    }
}
