package com.proyecto_petplate.petplate.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "usuarios_recomendaciones")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_like")
    private int recommendationId;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private User recommendationUser;

    @ManyToOne
    @JoinColumn(name = "fk_receta", nullable = false)
    private Recipe recommendationRecipe;
}
