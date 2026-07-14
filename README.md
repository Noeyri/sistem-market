# Sistem Market

Proyecto Java Jakarta EE (Servlet + JSP + JSTL) con patron MVC, DAO y Service.

## Requisitos
- JDK 17+
- Maven 3.8+
- MySQL 8+
- Un servidor de aplicaciones compatible con Jakarta EE 10 (Tomcat 10.1+, Wildfly 27+, etc.)
  **Importante:** Tomcat 9 o inferior usa el paquete `javax.*` y NO es compatible. Se necesita Tomcat 10.1+.

## Crear la base de datos
Ejecuta el script `database.sql` en MySQL:

## Estructura del proyecto

```
sistem-market/
│
├── pom.xml                        ← Configuración Maven (Jakarta EE 10, MySQL, BCrypt)
├── database.sql                   ← Script de creación y datos de prueba
│
├── src/main/java/com/sistemmarket/
│   │
│   ├── model/                     ← Entidades del dominio (POJO)
│   │   ├── Usuario.java
│   │   ├── Producto.java
│   │   ├── Carrito.java
│   │   └── DetalleCarrito.java
│   │
│   ├── dto/                       ← Objetos de transferencia (solo lectura agregada)
│   │   ├── EstadisticasAdminDTO.java
│   │   ├── EstadisticasUsuarioDTO.java
│   │   └── ProductoVendidoDTO.java
│   │
│   ├── dao/                       ← Acceso a datos (JDBC + PreparedStatement)
│   │   ├── DAOFactory.java        ← Factory Method: entrega instancias de los DAO
│   │   ├── UsuarioDAO.java
│   │   ├── ProductoDAO.java
│   │   └── CarritoDAO.java
│   │
│   ├── service/                   ← Lógica de negocio, validaciones y transacciones
│   │   ├── UsuarioService.java
│   │   ├── ProductoService.java
│   │   ├── CarritoService.java
│   │   ├── PedidoService.java
│   │   └── EstadisticasService.java
│   │
│   ├── controller/                ← Servlets (un servlet por recurso/acción)
│   │   ├── LoginController.java
│   │   ├── LogoutController.java
│   │   ├── DashboardController.java
│   │   ├── ProductoController.java
│   │   ├── UsuarioController.java
│   │   ├── CatalogoController.java
│   │   ├── CarritoController.java
│   │   ├── PedidoController.java
│   │   ├── ConfirmarCompraServlet.java
│   │   └── ActualizarPedidoServlet.java
│   │
│   ├── filter/                    ← Seguridad transversal
│   │   ├── AuthFilter.java        ← Exige sesión activa
│   │   └── AdminFilter.java       ← Exige rol ADMIN
│   │
│   └── util/
│       ├── ConexionBD.java        ← Fabrica conexiones JDBC (parametros centralizados)
│       ├── PasswordUtil.java      ← Hash y verificacion BCrypt
│       └── FlashMessage.java      ← Mensajes de exito/error de un solo uso via sesion
│
└── src/main/webapp/
    ├── index.jsp                  ← Redirige a /login
    ├── css/
    │   ├── styles.css             ← Estilos base (paleta, tipografia, componentes)
    │   └── dashboard.css          ← Estilos especificos del panel de estadisticas
    │
    └── WEB-INF/
        ├── web.xml                ← Mapeo de servlets y filtros
        └── views/
            ├── _header.jsp        ← Cabecera + navegacion + dropdown + mensajes flash
            ├── _footer.jsp        ← Pie de pagina
            ├── login.jsp
            ├── dashboard.jsp      ← Vista condicional segun rol (KPIs + graficos)
            ├── productos.jsp / producto_form.jsp   ← CRUD admin
            ├── usuarios.jsp / usuario_form.jsp     ← CRUD admin
            ├── catalogo.jsp       ← Catalogo con busqueda y filtro de categoria
            ├── carrito.jsp        ← Carrito con empty state
            └── pedidos.jsp        ← Historial de pedidos (vista dual admin/usuario)
```

## Funcionalidades
1. **Login/Logout** con sesion HTTP y contrasenas encriptadas con BCrypt.
2. **CRUD de productos** (solo ADMIN): crear, listar, editar, eliminar.
3. **CRUD de usuarios** (solo ADMIN): crear, listar, editar, cambiar password, eliminar.
4. **Carrito de compras** (solo USUARIO): agregar productos, quitar, vaciar, finalizar compra.
5. **Dashboard** que cambia segun el rol (ADMIN ve resumen de productos, USUARIO ve acceso rapido al carrito).

## Seguridad implementada
- `AuthFilter`: bloquea el acceso a rutas protegidas si no hay sesion activa.
- `AdminFilter`: bloquea `/productos` y `/usuarios` a usuarios que no sean ADMIN.
- Contrasenas nunca se guardan en texto plano (BCrypt).
