# Sistem Market

Proyecto Java Jakarta EE (Servlet + JSP + JSTL) con patron MVC, DAO y Service.

## Requisitos
- JDK 17+
- Maven 3.8+
- MySQL 8+
- Un servidor de aplicaciones compatible con Jakarta EE 10 (Tomcat 10.1+, Wildfly 27+, etc.)
  **Importante:** Tomcat 9 o inferior usa el paquete `javax.*` y NO es compatible. Se necesita Tomcat 10.1+.

## Pasos para ejecutar

### 1. Crear la base de datos
Ejecuta el script `database.sql` en tu servidor MySQL:

```bash
mysql -u root -p < database.sql
```

Esto crea la base `sistem_market`, sus tablas, y los usuarios de prueba:
- **admin / 123456** (rol ADMIN)
- **user / 123456** (rol USUARIO)

### 2. Configurar la conexion a MySQL
Edita `src/main/java/com/sistemmarket/util/ConexionBD.java` y ajusta usuario/password de tu MySQL local:

```java
private static final String USUARIO = "root";
private static final String PASSWORD = "root";
```

### 3. Compilar el proyecto

```bash
mvn clean package
```

Esto genera `target/sistem-market.war`.

### 4. Desplegar
Copia `target/sistem-market.war` a la carpeta `webapps` de Tomcat 10.1+, o despliega desde tu IDE (IntelliJ/Eclipse) usando un servidor Jakarta EE 10.

### 5. Acceder
```
http://localhost:8080/sistem-market/
```

## Estructura del proyecto

```
sistem-market/
├── pom.xml
├── database.sql
└── src/main/
    ├── java/com/sistemmarket/
    │   ├── model/        -> Usuario, Producto, Carrito, DetalleCarrito
    │   ├── dao/           -> UsuarioDAO, ProductoDAO, CarritoDAO
    │   ├── service/       -> UsuarioService, ProductoService, CarritoService
    │   ├── controller/    -> Servlets (Login, Logout, Dashboard, Producto, Usuario, Carrito)
    │   ├── filter/        -> AuthFilter (sesion), AdminFilter (rol ADMIN)
    │   └── util/          -> ConexionBD, PasswordUtil (BCrypt)
    └── webapp/
        ├── index.jsp
        └── WEB-INF/
            ├── web.xml
            └── views/     -> login.jsp, dashboard.jsp, productos.jsp,
                               producto_form.jsp, usuarios.jsp, usuario_form.jsp,
                               carrito.jsp, _header.jspf
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
