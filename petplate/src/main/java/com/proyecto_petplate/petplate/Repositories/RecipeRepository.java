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

    // Método para obtener recetas por categoría
    @Query("SELECT r FROM recetas r WHERE r.recipeCategory.categoryName = :categoryName")
    java.util.Optional<java.util.List<Recipe>> findByCategoryName(@Param("categoryName") String categoryName);

    // Método para obtener recetas por categoría y subcategoría
    @Query("SELECT r FROM recetas r WHERE r.recipeCategory.categoryName = :categoryName AND r.recipeCategory.subcategoryName = :subcategoryName")
    java.util.Optional<java.util.List<Recipe>> findByCategoryAndSubCategory(
    @Param("categoryName") String categoryName,
    @Param("subcategoryName") String subcategoryName);

    // Método para obtener todas las recetas de un usuario específico
    @Query("SELECT r FROM recetas r WHERE r.recipeUser = :user")
    java.util.Optional<java.util.List<Recipe>> findByUser(@Param("user") User user);


    @Query("SELECT r FROM recetas r WHERE r.recipeCategory.categoryName = :categoryName AND r.recipeCreatedDate >= :dateLimit ORDER BY r.recipeScore DESC")
    java.util.List<Recipe> findTopRecipeByCategory(@Param("categoryName") String categoryName, @Param("dateLimit") java.util.Date dateLimit);
}
