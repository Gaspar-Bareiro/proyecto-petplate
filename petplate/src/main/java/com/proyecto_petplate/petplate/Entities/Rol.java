package com.proyecto_petplate.petplate.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Mapeo de AUTO_INCREMENT
    @Column(name = "id_rol")
    private int rolId;

    @Column(name = "nombre_rol", nullable = false)
    @Enumerated(EnumType.STRING)  // Si `nombre_rol` es de tipo ENUM en la BD
    private EnumRolName rolName;
}

