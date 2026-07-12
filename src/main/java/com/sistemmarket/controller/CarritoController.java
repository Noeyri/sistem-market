package com.sistemmarket.controller;

import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.CarritoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CarritoController extends HttpServlet {

    private final CarritoService carritoService = new CarritoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "ver";

        switch (accion) {
            case "eliminar":
                int detalleId = Integer.parseInt(req.getParameter("id"));
                carritoService.eliminarDetalle(detalleId);
                resp.sendRedirect(req.getContextPath() + "/carrito");
                break;
            case "vaciar":
                carritoService.vaciarCarrito(usuario.getId());
                resp.sendRedirect(req.getContextPath() + "/carrito");
                break;
            case "finalizar":
                try {
                    carritoService.finalizarCompra(usuario.getId());
                    resp.sendRedirect(req.getContextPath() + "/carrito?mensaje=compra_finalizada");
                } catch (IllegalStateException e) {
                    req.setAttribute("error", e.getMessage());
                    Carrito carrito = carritoService.obtenerCarrito(usuario.getId());
                    req.setAttribute("carrito", carrito);
                    req.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(req, resp);
                }
                break;
            default:
                Carrito carrito = carritoService.obtenerCarrito(usuario.getId());
                req.setAttribute("carrito", carrito);
                req.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        int productoId = Integer.parseInt(req.getParameter("productoId"));
        int cantidad = Integer.parseInt(req.getParameter("cantidad"));

        try {
            carritoService.agregarProducto(usuario.getId(), productoId, cantidad);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        // Vuelve al catalogo (de donde vino la peticion), no al carrito
        resp.sendRedirect(req.getContextPath() + "/catalogo");
    }
}