package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RecipeIngredientRelationshipRepository  extends JpaRepository<RecipeIngredientRelationship, Integer>{
    @Modifying
    @Transactional
    @Query("DELETE FROM recetas_ingredientes r WHERE r.recipe.id = :recipeId")
    void deleteByRecipeId(int recipeId);
}
