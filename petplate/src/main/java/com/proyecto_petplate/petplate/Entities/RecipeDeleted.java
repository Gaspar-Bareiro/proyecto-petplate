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
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recetas_borradas")
public class RecipeDeleted {
    
    @Id
    @Column(name = "id_receta")
    private int recipeId;

    @Column(name = "titulo", nullable = false, length = 200)
    private String recipeTitle;

    @Column(name = "descripcion", nullable = false)
    private String recipeDescription;

    @Column(name = "img_receta", length = 255)
    private String recipeImg;

    @Column(name = "fecha_publicacion", nullable = false)
    private Date recipeCreatedDate;

    @Column(name = "contador_de_recomendaciones", nullable = false)
    private int recipeScore;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private User recipeUser;

    @ManyToOne
    @JoinColumn(name = "fk_categoria",nullable = false)
    private Category recipeCategory;

    // Constructor que recibe una receta
    public RecipeDeleted(Recipe recipe) {
        this.recipeId = recipe.getRecipeId();  // Copiando el mismo ID
        this.recipeTitle = recipe.getRecipeTitle();
        this.recipeDescription = recipe.getRecipeDescription();
        this.recipeImg = recipe.getRecipeImg();
        this.recipeCreatedDate = recipe.getRecipeCreatedDate();
        this.recipeScore = recipe.getRecipeScore();
        this.recipeUser = recipe.getRecipeUser();
        this.recipeCategory = recipe.getRecipeCategory();
    }
}

