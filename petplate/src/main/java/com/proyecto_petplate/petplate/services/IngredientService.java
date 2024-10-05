package com.proyecto_petplate.petplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.Entities.Ingredient;
import com.proyecto_petplate.petplate.Repositories.IngredientRepository;



@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepo;

    private java.util.List<Ingredient> ingredientesList;
    private String[] ingredientesArray;
    private boolean firstTime = true;

    //devuelve un arreglo con todos los ingredientes en la base de datos
    public java.util.List<Ingredient> obtenerIngredientesDB(){
        if (firstTime) {
            refreshIngredientArray();
            firstTime = false;
        }
        return ingredientesList;
    }

    // Devuelve un arreglo con los nombres de los ingredientes
    public String[] obtenerNombresIngredientes() {
        if (firstTime) {
            refreshIngredientArray();
            firstTime = false;
        }
        return ingredientesArray;
    }

    //actualiza el arreglo de ingredientes. es publico por si se inserta un ingrediente este pueda ser llamado
    public void refreshIngredientArray() {  // Obtener el Set
        ingredientesList = ingredientRepo.findAll();  // Convertir a array
        // Convierte la lista de ingredientes en un arreglo de nombres
        ingredientesArray = ingredientesList.stream()
            .map(Ingredient::getIngredientName) // Extrae el nombre de cada ingrediente
            .toArray(String[]::new); // Convierte a un arreglo
    }

    public Ingredient obtenerIngredientePorNombre(String ingredientName) {
        // Verifica si la lista de ingredientes ya está cargada
        if (firstTime) {
            refreshIngredientArray();
            firstTime = false;
        }
    
        // Busca el ingrediente en la lista
        return ingredientesList.stream()
                .filter(ingrediente -> ingrediente.getIngredientName().equalsIgnoreCase(ingredientName))
                .findFirst()
                .orElse(null); // Devuelve null si no se encuentra el ingrediente
    }
}
