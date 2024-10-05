package com.proyecto_petplate.petplate.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.proyecto_petplate.petplate.Entities.Ingredient;




@Repository
public interface IngredientRepository extends JpaRepository <Ingredient, Integer>{

    @NonNull
    public java.util.List<Ingredient> findAll();

}
