package com.sistemmarket.service;

import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.dao.ProductoDAO;
import com.sistemmarket.model.Producto;

import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO = DAOFactory.getProductoDAO();

    public List<Producto> listarTodos() {
        return productoDAO.listarTodos();
    }

    public Producto buscarPorId(int id) {
        return productoDAO.buscarPorId(id);
    }

    public void crear(Producto p) {
        productoDAO.insertar(p);
    }

    public void actualizar(Producto p) {
        productoDAO.actualizar(p);
    }

    public void eliminar(int id) {
        productoDAO.eliminar(id);
    }
}
