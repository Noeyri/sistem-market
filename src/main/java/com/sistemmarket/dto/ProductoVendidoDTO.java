package com.sistemmarket.dto;

public class ProductoVendidoDTO {
    private String nombre;
    private int cantidadVendida;
    private String colorHex;
    private int porcentajeBarra;  // relativo al producto mas vendido (para grafico de barras)
    private int porcentajeTotal;  // relativo a la suma de todos (para grafico circular)

    public ProductoVendidoDTO() {}

    public ProductoVendidoDTO(String nombre, int cantidadVendida) {
        this.nombre = nombre;
        this.cantidadVendida = cantidadVendida;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(int cantidadVendida) { this.cantidadVendida = cantidadVendida; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public int getPorcentajeBarra() { return porcentajeBarra; }
    public void setPorcentajeBarra(int porcentajeBarra) { this.porcentajeBarra = porcentajeBarra; }

    public int getPorcentajeTotal() { return porcentajeTotal; }
    public void setPorcentajeTotal(int porcentajeTotal) { this.porcentajeTotal = porcentajeTotal; }
}