package com.sistemmarket.service;

import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.dao.UsuarioDAO;
import com.sistemmarket.model.Usuario;
import com.sistemmarket.util.PasswordUtil;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();

    public Usuario autenticar(String username, String password) {
        Usuario u = usuarioDAO.buscarPorUsername(username);
        if (u != null && PasswordUtil.verificar(password, u.getPassword())) {
            return u;
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public void crear(Usuario u, String passwordPlano) {
        u.setPassword(PasswordUtil.hash(passwordPlano));
        usuarioDAO.insertar(u);
    }

    public void actualizar(Usuario u) {
        usuarioDAO.actualizar(u);
    }

    public void cambiarPassword(int id, String nuevoPasswordPlano) {
        usuarioDAO.actualizarPassword(id, PasswordUtil.hash(nuevoPasswordPlano));
    }

    public void eliminar(int id) {
        usuarioDAO.eliminar(id);
    }

    public boolean existeUsername(String username) {
        return usuarioDAO.buscarPorUsername(username) != null;
    }
}
