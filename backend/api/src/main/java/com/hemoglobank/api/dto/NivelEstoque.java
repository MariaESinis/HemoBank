package com.hemoglobank.api.dto;

/** Situação do estoque de uma tipagem sanguínea. */
public record NivelEstoque(String tipagem, long bolsas, Nivel nivel) {

    public enum Nivel {
        CRITICO("Crítico", "critico"),
        ESTAVEL("Estável", "aviso"),
        ABASTECIDO("Abastecido", "ok");

        private final String rotulo;
        private final String css;

        Nivel(String rotulo, String css) {
            this.rotulo = rotulo;
            this.css = css;
        }

        public String getRotulo() { return rotulo; }
        public String getCss() { return css; }
    }
}
