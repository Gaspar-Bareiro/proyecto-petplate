package com.proyecto_petplate.petplate.Entities;

public enum EnumUnidadMedida {
    Unidad,
    Kg,
    gr,
    L,
    ml,
    Pizca,
    Cucharada,
    Taza;

    public String getValor() {
        return this.name(); // Devuelve el nombre del enum en minúsculas
    }

    // Método para verificar si una unidad de medida es válida
    public static boolean isValidUnit(String unidad) {
        for (EnumUnidadMedida medida : EnumUnidadMedida.values()) {
            if (medida.getValor().equalsIgnoreCase(unidad)) {
                return true; // La unidad de medida es válida
            }
        }
        return false; // La unidad de medida no es válida
    }
}
