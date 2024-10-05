package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Integer>{

    // Método para verificar si existe una categoría por nombre y subcategoría
    boolean existsByCategoryNameAndSubcategoryName(String categoryName, String subcategoryName);

    // Método para obtener una categoría por nombre y subcategoría
    Category findByCategoryNameAndSubcategoryName(String categoryName, String subcategoryName);

}
