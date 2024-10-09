package com.proyecto_petplate.petplate.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity(name = "recetas_borradas_ingredientes")
public class RecipeIngredientRelationshipDeleted {

    @Id
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

    // Constructor que recibe una instancia de RecipeIngredientRelationship
    public RecipeIngredientRelationshipDeleted(RecipeIngredientRelationship original) {
        this.id = original.getId();  // Copiando el mismo ID
        this.quantity = original.getQuantity();
        this.unitOfMeasurement = original.getUnitOfMeasurement();
        this.recipe = original.getRecipe();
        this.ingredient = original.getIngredient();
    }

}