<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Sistem Market - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="login-body">
    <div class="login-caja">
        <h1>Sistem Market</h1>
        <p class="subtitulo">Ingresa a tu cuenta para continuar</p>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <label for="username">Usuario</label>
            <input type="text" id="username" name="username" required autofocus>

            <label for="password">Contrasena</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Ingresar</button>
        </form>
        <div class="info">admin / 123456 &nbsp;|&nbsp; user / 123456</div>
    </div>
</body>
</html>