package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationshipDeleted;

@Repository
public interface RecipeIngredientRelationshipDeletedRepository extends JpaRepository<RecipeIngredientRelationshipDeleted , Integer> {
    
}
