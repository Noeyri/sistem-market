package com.sistemmarket.dao;

/**
 * Factory Method: centraliza la creacion de los DAO.
 * Los Service piden sus DAO aqui en vez de instanciarlos con "new".
 */
public class DAOFactory {

    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final CarritoDAO carritoDAO = new CarritoDAO();

    private DAOFactory() {
        // Constructor privado: no se instancia
    }

    public static UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public static ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    public static CarritoDAO getCarritoDAO() {
        return carritoDAO;
    }
}