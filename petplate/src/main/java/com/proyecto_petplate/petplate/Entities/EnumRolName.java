package com.proyecto_petplate.petplate.Entities;

public enum EnumRolName {
    ADMINISTRADOR, 
    AUDITOR, 
    USUARIO;

    public String getValorBD() {
        // Convierte la primera letra a mayúscula y el resto a minúscula
        String rol = this.name().toLowerCase(); // Convierte todo a minúsculas
        return rol.substring(0, 1).toUpperCase() + rol.substring(1); // Capitaliza la primera letra
    }

    public String getValor() {
        return this.name(); // Devuelve el nombre del enum en mayúsculas
    }

}
