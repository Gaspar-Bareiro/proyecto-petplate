package com.proyecto_petplate.petplate.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int userId;

    @Column(name = "usuario", unique = true, nullable = false, length = 20)
    private String userName;

    @Column(name = "email", unique = true, nullable = false, length = 254)
    private String userEmail;

    @Column(name = "contrasena", nullable = false)
    private String userPassword;

    @Column(name = "img_perfil")  // Almacena la URL o ruta de la imagen
    private String userImg;

    @ManyToOne
    @JoinColumn(name = "fk_rol", nullable = false)
    private Rol userRol;

}
