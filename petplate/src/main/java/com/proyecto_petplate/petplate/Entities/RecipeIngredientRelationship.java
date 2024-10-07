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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recetas_ingredientes")
public class RecipeIngredientRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta_ingrediente")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private double quantity;

    @Column(name = "unidad_medida",nullable = false,length = 15)
    private String unitOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "fk_receta", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "fk_ingrediente",nullable = false)
    private Ingredient ingredient;


}
