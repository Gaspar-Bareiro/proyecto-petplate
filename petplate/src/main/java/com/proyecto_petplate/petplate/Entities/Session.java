package com.proyecto_petplate.petplate.Entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
@Builder
@Entity
@Table(name = "sesiones")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private int sessionId;

    @Column(name = "token_sesion", nullable = false)
    private String sesionToken;

    @Column(name = "fecha_inicio", nullable = false)
    private Date createdDate; // Cambiar a LocalDateTime

    @Column(name = "fecha_fin", nullable = false)
    private Date expiredDate; // Cambiar a LocalDateTime

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private User sesionUser;
}
