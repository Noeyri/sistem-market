package com.sistemmarket.service;

import com.sistemmarket.dao.CarritoDAO;
import com.sistemmarket.dao.DAOFactory;
import com.sistemmarket.model.Carrito;

import java.util.List;

public class PedidoService {

    private final CarritoDAO carritoDAO = DAOFactory.getCarritoDAO();

    public List<Carrito> listarPedidosDeUsuario(int usuarioId) {
        return carritoDAO.listarPorUsuario(usuarioId);
    }

    public List<Carrito> listarTodosLosPedidos() {
        return carritoDAO.listarTodosFinalizados();
    }

    //El CLIENTE confirma su compra: PENDIENTE -> PROCESANDO.
    public void confirmarCompra(int pedidoId, int usuarioId) {
        Carrito pedido = carritoDAO.buscarPorId(pedidoId);
        if (pedido == null || pedido.getUsuarioId() != usuarioId) {
            throw new IllegalArgumentException("El pedido no existe o no te pertenece");
        }
        if (!"PENDIENTE".equals(pedido.getEstadoPedido())) {
            throw new IllegalStateException("El pedido ya fue confirmado anteriormente");
        }
        carritoDAO.actualizarEstadoPedido(pedidoId, "PROCESANDO");
    }

    // El ADMIN marca el pedido como entregado: PROCESANDO -> ENTREGADO.
    public void marcarComoEntregado(int pedidoId) {
        Carrito pedido = carritoDAO.buscarPorId(pedidoId);
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        if (!"PROCESANDO".equals(pedido.getEstadoPedido())) {
            throw new IllegalStateException("El pedido debe estar en PROCESANDO para marcarlo como entregado");
        }
        carritoDAO.actualizarEstadoPedido(pedidoId, "ENTREGADO");
    }
}