package com.sistemmarket.dao;

import com.sistemmarket.dto.ProductoVendidoDTO;
import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.DetalleCarrito;
import com.sistemmarket.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {

    // Obtiene el carrito ACTIVO del usuario, o lo crea si no existe.
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
    
    public void finalizarCarrito(Connection con, int carritoId) throws SQLException {
        String sql = "UPDATE carrito SET estado = 'FINALIZADO' WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carritoId);
            ps.executeUpdate();
        }
    }
    
    //Pedidos (carritos finalizados) de un usuario especifico.
    public List<Carrito> listarPorUsuario(int usuarioId) {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE usuario_id = ? AND estado = 'FINALIZADO' ORDER BY fecha_creacion DESC";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Carrito c = new Carrito();
                    c.setId(rs.getInt("id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setEstado(rs.getString("estado"));
                    c.setEstadoPedido(rs.getString("estado_pedido"));
                    c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    c.setDetalles(listarDetalles(con, c.getId()));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos del usuario", e);
        }
        return lista;
    }

    //Todos los pedidos (de todos los clientes) - solo para ADMIN.
    public List<Carrito> listarTodosFinalizados() {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre AS usuario_nombre FROM carrito c " +
                "JOIN usuarios u ON c.usuario_id = u.id " +
                "WHERE c.estado = 'FINALIZADO' ORDER BY c.fecha_creacion DESC";
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Carrito c = new Carrito();
                c.setId(rs.getInt("id"));
                c.setUsuarioId(rs.getInt("usuario_id"));
                c.setEstado(rs.getString("estado"));
                c.setEstadoPedido(rs.getString("estado_pedido"));
                c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                c.setUsuarioNombre(rs.getString("usuario_nombre"));
                c.setDetalles(listarDetalles(con, c.getId()));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los pedidos", e);
        }
        return lista;
    }
    
    //Cambia el estado de gestion de un pedido (PENDIENTE, PROCESANDO, ENTREGADO).
    public void actualizarEstadoPedido(int carritoId, String nuevoEstado) {
       String sql = "UPDATE carrito SET estado_pedido = ? WHERE id = ?";
       try (Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setString(1, nuevoEstado);
           ps.setInt(2, carritoId);
           ps.executeUpdate();
       } catch (SQLException e) {
           throw new RuntimeException("Error al actualizar estado del pedido", e);
       }
   }

   // Trae un pedido por su id, incluyendo el usuario_id (para validar propiedad).
   public Carrito buscarPorId(int carritoId) {
       String sql = "SELECT * FROM carrito WHERE id = ?";
       try (Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setInt(1, carritoId);
           try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) {
                   Carrito c = new Carrito();
                   c.setId(rs.getInt("id"));
                   c.setUsuarioId(rs.getInt("usuario_id"));
                   c.setEstado(rs.getString("estado"));
                   c.setEstadoPedido(rs.getString("estado_pedido"));
                   return c;
               }
           }
       } catch (SQLException e) {
           throw new RuntimeException("Error al buscar pedido", e);
       }
       return null;
   }
   
   public int contarFinalizados() {
        String sql = "SELECT COUNT(*) FROM carrito WHERE estado = 'FINALIZADO'";
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar pedidos", e);
        }
        return 0;
    }

    public int contarPorUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM carrito WHERE usuario_id = ? AND estado = 'FINALIZADO'";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar pedidos del usuario", e);
        }
        return 0;
    }

    //Ultimos N pedidos de TODOS los clientes (para el resumen del admin).
    public List<Carrito> listarRecientes(int limite) {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT c.*, u.nombre AS usuario_nombre FROM carrito c " +
                "JOIN usuarios u ON c.usuario_id = u.id " +
                "WHERE c.estado = 'FINALIZADO' ORDER BY c.fecha_creacion DESC LIMIT ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Carrito c = new Carrito();
                    c.setId(rs.getInt("id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setEstado(rs.getString("estado"));
                    c.setEstadoPedido(rs.getString("estado_pedido"));
                    c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    c.setUsuarioNombre(rs.getString("usuario_nombre"));
                    c.setDetalles(listarDetalles(con, c.getId()));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos recientes", e);
        }
        return lista;
    }

    //Top N productos con mas unidades vendidas (solo pedidos FINALIZADO).
    public List<ProductoVendidoDTO> listarTopProductosVendidos(int limite) {
        List<ProductoVendidoDTO> lista = new ArrayList<>();
        String sql = "SELECT p.nombre AS nombre, SUM(dc.cantidad) AS total_vendido " +
                "FROM detalle_carrito dc " +
                "JOIN carrito c ON dc.carrito_id = c.id " +
                "JOIN productos p ON dc.producto_id = p.id " +
                "WHERE c.estado = 'FINALIZADO' " +
                "GROUP BY p.id, p.nombre " +
                "ORDER BY total_vendido DESC LIMIT ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ProductoVendidoDTO(rs.getString("nombre"), rs.getInt("total_vendido")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar top productos vendidos", e);
        }
        return lista;
    }

    //Ultimos N pedidos de UN usuario especifico (para su propio dashboard).
    public List<Carrito> listarUltimosPedidosUsuario(int usuarioId, int limite) {
        List<Carrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM carrito WHERE usuario_id = ? AND estado = 'FINALIZADO' " +
                "ORDER BY fecha_creacion DESC LIMIT ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Carrito c = new Carrito();
                    c.setId(rs.getInt("id"));
                    c.setUsuarioId(rs.getInt("usuario_id"));
                    c.setEstado(rs.getString("estado"));
                    c.setEstadoPedido(rs.getString("estado_pedido"));
                    c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    c.setDetalles(listarDetalles(con, c.getId()));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ultimos pedidos del usuario", e);
        }
        return lista;
    }

    //Suma de lo gastado por un usuario en todos sus pedidos FINALIZADO.
    public java.math.BigDecimal calcularTotalGastadoUsuario(int usuarioId) {
        String sql = "SELECT COALESCE(SUM(dc.precio_unitario * dc.cantidad), 0) AS total " +
                "FROM detalle_carrito dc " +
                "JOIN carrito c ON dc.carrito_id = c.id " +
                "WHERE c.usuario_id = ? AND c.estado = 'FINALIZADO'";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular total gastado", e);
        }
        return java.math.BigDecimal.ZERO;
    }
    
    public int contarPorUsuarioMesActual(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM carrito WHERE usuario_id = ? AND estado = 'FINALIZADO' " +
                "AND MONTH(fecha_creacion) = MONTH(CURDATE()) AND YEAR(fecha_creacion) = YEAR(CURDATE())";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar pedidos del mes", e);
        }
        return 0;
    }

    public java.math.BigDecimal calcularGastoUsuarioMesActual(int usuarioId) {
        String sql = "SELECT COALESCE(SUM(dc.precio_unitario * dc.cantidad), 0) AS total " +
                "FROM detalle_carrito dc JOIN carrito c ON dc.carrito_id = c.id " +
                "WHERE c.usuario_id = ? AND c.estado = 'FINALIZADO' " +
                "AND MONTH(c.fecha_creacion) = MONTH(CURDATE()) AND YEAR(c.fecha_creacion) = YEAR(CURDATE())";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular gasto del mes", e);
        }
        return java.math.BigDecimal.ZERO;
    }
}
