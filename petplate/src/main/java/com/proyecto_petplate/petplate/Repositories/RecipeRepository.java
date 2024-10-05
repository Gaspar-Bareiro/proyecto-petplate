package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto_petplate.petplate.Entities.Recipe;

public interface RecipeRepository extends JpaRepository <Recipe, Integer>{

}
