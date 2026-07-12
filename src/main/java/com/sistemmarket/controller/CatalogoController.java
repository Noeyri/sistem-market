package com.sistemmarket.controller;

import com.sistemmarket.model.Producto;
import com.sistemmarket.service.ProductoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Catalogo de productos para el USUARIO (solo lectura + boton "Agregar al carrito").
 * Distinto de ProductoController, que es el CRUD exclusivo del ADMIN.
 */
public class CatalogoController extends HttpServlet {

    private final ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Producto> productos = productoService.listarTodos();
        req.setAttribute("productos", productos);
        req.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(req, resp);
    }
}