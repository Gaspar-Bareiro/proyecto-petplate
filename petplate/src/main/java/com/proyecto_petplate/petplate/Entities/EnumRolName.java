package com.proyecto_petplate.petplate.Entities;

public enum EnumRolName {
    Administrador, 
    Auditor, 
    Usuario;

    public String getValor() {
        return this.name(); // Devuelve el nombre del enum en mayúsculas
    }

}
