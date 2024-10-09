package com.proyecto_petplate.petplate.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto_petplate.petplate.Entities.Ingredient;
import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeIngredientRelationshipRepository  extends JpaRepository<RecipeIngredientRelationship, Integer>{
    @Modifying
    @Transactional
    @Query("DELETE FROM recetas_ingredientes r WHERE r.recipe.id = :recipeId")
    void deleteByRecipeId(int recipeId);

    // Método para obtener los ingredientes de una receta por su ID
    @Query("SELECT r.ingredient FROM recetas_ingredientes r WHERE r.recipe.id = :recipeId")
    java.util.List<Ingredient> findIngredientsByRecipeId(@Param("recipeId") int recipeId);

    // Método para obtener todas las recetas que contengan todos los ingredientes dados
    @Query("SELECT r.recipe FROM recetas_ingredientes r " +
        "WHERE r.ingredient IN :ingredientes " +
        "GROUP BY r.recipe " +
        "HAVING COUNT(r.ingredient) = :size")
    java.util.Optional<java.util.List<Recipe>> findRecipesByAllIngredients(
            @Param("ingredientes") java.util.List<Ingredient> ingredientes,
            @Param("size") long size);
}
