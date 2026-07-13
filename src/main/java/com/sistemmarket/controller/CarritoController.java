package com.sistemmarket.controller;

import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.CarritoService;
import com.sistemmarket.util.FlashMessage;
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
                FlashMessage.success(req, "Producto quitado del carrito.");
                resp.sendRedirect(req.getContextPath() + "/carrito");
                break;

            case "vaciar":
                carritoService.vaciarCarrito(usuario.getId());
                FlashMessage.success(req, "Carrito vaciado correctamente.");
                resp.sendRedirect(req.getContextPath() + "/carrito");
                break;

            case "finalizar":
                try {
                    carritoService.finalizarCompra(usuario.getId());
                    FlashMessage.success(req, "Compra finalizada con exito. Gracias por tu pedido.");
                } catch (IllegalStateException e) {
                    FlashMessage.error(req, e.getMessage());
                }
                resp.sendRedirect(req.getContextPath() + "/carrito");
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
            FlashMessage.success(req, "Producto agregado al carrito.");
        } catch (IllegalArgumentException e) {
            FlashMessage.error(req, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/catalogo");
    }
}