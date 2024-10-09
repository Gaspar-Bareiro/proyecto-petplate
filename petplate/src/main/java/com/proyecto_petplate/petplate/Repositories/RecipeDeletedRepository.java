package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.RecipeDeleted;

@Repository
public interface RecipeDeletedRepository extends JpaRepository<RecipeDeleted, Integer>{

}
