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
    private int user_id;

    @Column(name = "usuario", unique = true, nullable = false, length = 20)
    private String user_name;

    @Column(name = "email", unique = true, nullable = false, length = 254)
    private String user_email;

    @Column(name = "contrasena", nullable = false)
    private String user_password;

    @Column(name = "img_perfil")  // Almacena la URL o ruta de la imagen
    private String user_img;

    @ManyToOne
    @JoinColumn(name = "fk_rol", nullable = false)
    private Rol user_rol;

}
