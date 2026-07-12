package com.sistemmarket.util;

import jakarta.servlet.http.HttpServletRequest;

public class FlashMessage {

    public static void success(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("flashSuccess", mensaje);
    }

    public static void error(HttpServletRequest req, String mensaje) {
        req.getSession().setAttribute("flashError", mensaje);
    }
}