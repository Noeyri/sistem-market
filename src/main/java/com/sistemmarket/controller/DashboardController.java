package com.sistemmarket.controller;

import com.sistemmarket.dto.EstadisticasAdminDTO;
import com.sistemmarket.dto.EstadisticasUsuarioDTO;
import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.EstadisticasService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DashboardController extends HttpServlet {

    private final EstadisticasService estadisticasService = new EstadisticasService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario.isAdmin()) {
            EstadisticasAdminDTO estadisticas = estadisticasService.obtenerEstadisticasAdmin();
            req.setAttribute("estadisticas", estadisticas);
        } else {
            EstadisticasUsuarioDTO estadisticas = estadisticasService.obtenerEstadisticasUsuario(usuario.getId());
            req.setAttribute("estadisticas", estadisticas);
        }

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}