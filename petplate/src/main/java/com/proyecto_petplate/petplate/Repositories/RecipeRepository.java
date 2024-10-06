package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.User;

public interface RecipeRepository extends JpaRepository <Recipe, Integer>{
    
    // Método para verificar si existe una receta con el nombre y usuario dados
    @Query("SELECT COUNT(r) > 0 FROM recetas r WHERE r.recipeTitle = :title AND r.recipeUser = :user")
    boolean existsByTitleAndUser(@Param("title") String title, @Param("user") User user);
}
