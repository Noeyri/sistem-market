package com.sistemmarket.controller;

import com.sistemmarket.model.Usuario;
import com.sistemmarket.service.UsuarioService;
import com.sistemmarket.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                req.getRequestDispatcher("/WEB-INF/views/usuario_form.jsp").forward(req, resp);
                break;
            case "editar":
                int idEditar = Integer.parseInt(req.getParameter("id"));
                Usuario usuario = usuarioService.buscarPorId(idEditar);
                req.setAttribute("usuarioEditar", usuario);
                req.getRequestDispatcher("/WEB-INF/views/usuario_form.jsp").forward(req, resp);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(req.getParameter("id"));
                usuarioService.eliminar(idEliminar);
                FlashMessage.success(req, "Usuario eliminado correctamente.");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
                break;
            default:
                req.setAttribute("usuarios", usuarioService.listarTodos());
                req.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String username = req.getParameter("username");
        String nombre = req.getParameter("nombre");
        String rol = req.getParameter("rol");
        String password = req.getParameter("password");

        if (idParam != null && !idParam.isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setId(Integer.parseInt(idParam));
            usuario.setUsername(username);
            usuario.setNombre(nombre);
            usuario.setRol(rol);
            usuarioService.actualizar(usuario);

            if (password != null && !password.isEmpty()) {
                usuarioService.cambiarPassword(usuario.getId(), password);
            }
            FlashMessage.success(req, "Usuario actualizado correctamente.");
        } else {
            if (usuarioService.existeUsername(username)) {
                FlashMessage.error(req, "El nombre de usuario ya existe.");
                req.getRequestDispatcher("/WEB-INF/views/usuario_form.jsp").forward(req, resp);
                return;
            }
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setNombre(nombre);
            usuario.setRol(rol);
            usuarioService.crear(usuario, password);
            FlashMessage.success(req, "Usuario creado correctamente.");
        }
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }
}
