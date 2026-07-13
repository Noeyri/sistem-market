package com.sistemmarket.dao;

import com.sistemmarket.model.Producto;
import com.sistemmarket.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id";
        try (Connection con = ConexionBD.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos", e);
        }
        return lista;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto", e);
        }
        return null;
    }

    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto", e);
        }
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, categoria = ?, precio = ?, stock = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    public void actualizarStock(int id, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        return p;
    }

    /*
      Descuenta stock de forma segura: solo resta si hay stock suficiente.
      Devuelve true si se pudo descontar, false si no habia stock suficiente.
     */
    public boolean descontarStock(Connection con, int productoId, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM productos";
        try (Connection con = ConexionBD.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar productos", e);
        }
        return 0;
    }

    public int contarStockBajo(int umbral) {
        String sql = "SELECT COUNT(*) FROM productos WHERE stock < ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, umbral);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar stock bajo", e);
        }
        return 0;
    }

    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY id";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos por categoria", e);
        }
        return lista;
    }
}
