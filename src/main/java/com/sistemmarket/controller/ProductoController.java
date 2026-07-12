package com.sistemmarket.controller;

import com.sistemmarket.model.Producto;
import com.sistemmarket.service.ProductoService;
import com.sistemmarket.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

public class ProductoController extends HttpServlet {

    private final ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                req.getRequestDispatcher("/WEB-INF/views/producto_form.jsp").forward(req, resp);
                break;
            case "editar":
                int idEditar = Integer.parseInt(req.getParameter("id"));
                Producto producto = productoService.buscarPorId(idEditar);
                req.setAttribute("producto", producto);
                req.getRequestDispatcher("/WEB-INF/views/producto_form.jsp").forward(req, resp);
                break;
            case "eliminar":
                int idEliminar = Integer.parseInt(req.getParameter("id"));
                productoService.eliminar(idEliminar);
                FlashMessage.success(req, "Producto eliminado correctamente.");
                resp.sendRedirect(req.getContextPath() + "/productos");
                break;
            default:
                req.setAttribute("productos", productoService.listarTodos());
                req.getRequestDispatcher("/WEB-INF/views/productos.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        BigDecimal precio = new BigDecimal(req.getParameter("precio"));
        int stock = Integer.parseInt(req.getParameter("stock"));

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);

        // al final de doPost, antes del redirect:
        if (idParam != null && !idParam.isEmpty()) {
            producto.setId(Integer.parseInt(idParam));
            productoService.actualizar(producto);
            FlashMessage.success(req, "Producto actualizado correctamente.");
        } else {
            productoService.crear(producto);
            FlashMessage.success(req, "Producto creado correctamente.");
        }
        resp.sendRedirect(req.getContextPath() + "/productos");
    }
}
