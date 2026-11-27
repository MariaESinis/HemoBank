package com.hemoglobank.api.model;

public enum StatusAgendamento {
    PENDENTE("Pendente", "aviso"),
    APROVADO("Aprovado", "info"),
    CONCLUIDO("Concluído", "ok"),
    CANCELADO("Cancelado", "cinza");

    private final String rotulo;
    private final String css;

    StatusAgendamento(String rotulo, String css) {
        this.rotulo = rotulo;
        this.css = css;
    }

    public String getRotulo() { return rotulo; }
    public String getCss() { return css; }

    /** Agendamentos que ainda ocupam a "vaga" do doador. */
    public boolean isEmAberto() {
        return this == PENDENTE || this == APROVADO;
    }
}
