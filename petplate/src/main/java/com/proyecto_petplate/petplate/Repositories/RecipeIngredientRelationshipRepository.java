package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;

public interface RecipeIngredientRelationshipRepository  extends JpaRepository<RecipeIngredientRelationship, Integer>{

}
