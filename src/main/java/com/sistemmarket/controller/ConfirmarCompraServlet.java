package com.sistemmarket.controller;

import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.PedidoService;
import com.sistemmarket.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ConfirmarCompraServlet extends HttpServlet {

    private final PedidoService pedidoService = new PedidoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int pedidoId = Integer.parseInt(req.getParameter("pedidoId"));

        try {
            pedidoService.confirmarCompra(pedidoId, usuario.getId());
            FlashMessage.success(req, "Compra confirmada. Tu pedido esta siendo procesado.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            FlashMessage.error(req, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/pedidos");
    }
}