package com.hemoglobank.api.model;

public enum ComponenteSanguineo {
    SANGUE_TOTAL("Sangue total"),
    PLAQUETAS("Plaquetas"),
    PLASMA("Plasma");

    private final String rotulo;

    ComponenteSanguineo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() { return rotulo; }
}
