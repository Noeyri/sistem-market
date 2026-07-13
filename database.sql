-- =====================================================
-- Sistem Market - Script de Base de Datos
-- =====================================================

DROP DATABASE IF EXISTS sistem_market;
CREATE DATABASE sistem_market CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sistem_market;

-- =====================================================
-- Tabla: usuarios
-- =====================================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') NOT NULL DEFAULT 'USUARIO'
);

-- =====================================================
-- Tabla: productos
-- =====================================================
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0
);

-- =====================================================
-- Tabla: carrito
-- =====================================================
CREATE TABLE carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    estado ENUM('ACTIVO', 'FINALIZADO') NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

ALTER TABLE carrito
    ADD COLUMN estado_pedido ENUM('PENDIENTE', 'PROCESANDO', 'ENTREGADO')
    NOT NULL DEFAULT 'PENDIENTE'
    AFTER estado;

-- Opcional: deja los pedidos ya existentes como PENDIENTE (valor por defecto ya los cubre)

-- =====================================================
-- Tabla: detalle_carrito
-- =====================================================
CREATE TABLE detalle_carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrito_id INT NOT NULL,
    producto_id INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_detalle_carrito FOREIGN KEY (carrito_id) REFERENCES carrito(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- =====================================================
-- Datos de prueba: usuarios
-- Password para ambos: 123456  (hash BCrypt)
-- =====================================================
INSERT INTO usuarios (username, password, nombre, rol) VALUES
('admin', '$2a$10$ahqvr7ArZ5ZyA.PXit7WZechh5b/vfaZnpv8c04h..VIlwfJKYap6', 'Administrador General', 'ADMIN'),
('user',  '$2a$10$ahqvr7ArZ5ZyA.PXit7WZechh5b/vfaZnpv8c04h..VIlwfJKYap6', 'Usuario de Prueba', 'USUARIO');

-- =====================================================
-- Datos de prueba: productos
-- =====================================================
INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
('Arroz Extra 1kg', 'Bolsa de arroz extra calidad, 1 kilogramo', 4.50, 100),
('Aceite Vegetal 1L', 'Botella de aceite vegetal, 1 litro', 8.90, 60),
('Leche Evaporada', 'Lata de leche evaporada 400g', 3.20, 150),
('Azucar Rubia 1kg', 'Bolsa de azucar rubia, 1 kilogramo', 4.00, 80),
('Fideos Spaghetti 500g', 'Paquete de fideos spaghetti', 2.80, 120),
('Atun en Lata', 'Lata de atun en aceite vegetal 170g', 5.50, 90);

-- =====================================================
-- Fin del script
-- =====================================================

-- Mas usuarios de prueba
INSERT INTO usuarios (username, password, nombre, rol) VALUES
('maria', '$2a$10$ahqvr7ArZ5ZyA.PXit7WZechh5b/vfaZnpv8c04h..VIlwfJKYap6', 'Maria Lopez', 'USUARIO'),
('carlos', '$2a$10$ahqvr7ArZ5ZyA.PXit7WZechh5b/vfaZnpv8c04h..VIlwfJKYap6', 'Carlos Ramirez', 'ADMIN');
-- password para ambos: 123456

-- Mas productos de prueba
INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
('Detergente 1kg', 'Detergente en polvo, bolsa de 1kg', 9.90, 40),
('Papel Higienico x4', 'Pack de 4 rollos de papel higienico', 6.50, 70),
('Cafe Instantaneo 100g', 'Frasco de cafe instantaneo', 12.00, 35),
('Galletas Soda x6', 'Paquete de galletas soda, 6 unidades', 3.50, 200),
('Jabon de Tocador', 'Jabon de tocador aroma floral', 2.20, 0),
('Yogurt Natural 1L', 'Botella de yogurt natural, 1 litro', 7.80, 25);