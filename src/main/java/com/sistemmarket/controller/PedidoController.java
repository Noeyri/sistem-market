package com.sistemmarket.controller;

import com.sistemmarket.model.Carrito;
import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.PedidoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class PedidoController extends HttpServlet {

    private final PedidoService pedidoService = new PedidoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Carrito> pedidos = usuario.isAdmin()
                ? pedidoService.listarTodosLosPedidos()
                : pedidoService.listarPedidosDeUsuario(usuario.getId());

        req.setAttribute("pedidos", pedidos);
        req.getRequestDispatcher("/WEB-INF/views/pedidos.jsp").forward(req, resp);
    }
}