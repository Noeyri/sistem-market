package com.sistemmarket.controller;

import com.sistemmarket.service.PedidoService;
import com.sistemmarket.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ActualizarPedidoServlet extends HttpServlet {

    private final PedidoService pedidoService = new PedidoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int pedidoId = Integer.parseInt(req.getParameter("pedidoId"));

        try {
            pedidoService.marcarComoEntregado(pedidoId);
            FlashMessage.success(req, "Pedido #" + pedidoId + " marcado como entregado.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            FlashMessage.error(req, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/pedidos");
    }
}