package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.Recommendation;
import com.proyecto_petplate.petplate.Entities.User;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    // Método personalizado para verificar si existe una recomendación por usuario y receta
    boolean existsByRecommendationUserAndRecommendationRecipe(User recommendationUser, Recipe recommendationRecipe);

    // Método personalizado para eliminar todas las recomendaciones asociadas a una receta
    void deleteByRecommendationRecipe(Recipe recommendationRecipe);
}
