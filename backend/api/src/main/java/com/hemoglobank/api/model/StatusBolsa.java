package com.hemoglobank.api.model;

public enum StatusBolsa {
    DISPONIVEL("Disponível", "ok"),
    RESERVADA("Reservada", "info"),
    UTILIZADA("Utilizada", "cinza"),
    DESCARTADA("Descartada", "critico");

    private final String rotulo;
    private final String css;

    StatusBolsa(String rotulo, String css) {
        this.rotulo = rotulo;
        this.css = css;
    }

    public String getRotulo() { return rotulo; }
    public String getCss() { return css; }
}
