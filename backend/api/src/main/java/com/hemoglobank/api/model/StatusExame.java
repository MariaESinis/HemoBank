package com.hemoglobank.api.model;

public enum StatusExame {
    SOLICITADO("Solicitado", "aviso"),
    EM_ANALISE("Em análise", "info"),
    CONCLUIDO("Concluído", "ok"),
    CANCELADO("Cancelado", "cinza");

    private final String rotulo;
    private final String css;

    StatusExame(String rotulo, String css) {
        this.rotulo = rotulo;
        this.css = css;
    }

    public String getRotulo() { return rotulo; }
    public String getCss() { return css; }

    public boolean isEmAberto() {
        return this == SOLICITADO || this == EM_ANALISE;
    }
}
