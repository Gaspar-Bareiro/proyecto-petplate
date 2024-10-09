package com.proyecto_petplate.petplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.Entities.Ingredient;
import com.proyecto_petplate.petplate.Repositories.IngredientRepository;

import java.util.Arrays;



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

    public java.util.List<Ingredient> obtenerListaIngredientesPorNombres(String[] ingredientesNombres) {
        java.util.List<Ingredient> ingredientesEncontrados = new java.util.ArrayList<>();

        // Itera sobre el arreglo de nombres de ingredientes
        Arrays.stream(ingredientesNombres)
            .forEach(nombre -> {
                Ingredient ingrediente = obtenerIngredientePorNombre(nombre); // Llama a tu método existente
                if (ingrediente != null) {
                    ingredientesEncontrados.add(ingrediente); // Si se encuentra el ingrediente, lo agrega a la lista
                }
            });

        return ingredientesEncontrados;
}

    // método que recibe una lista de ingredientes y devuelve un arreglo de nombres
    public String[] obtenerNombresDeIngredientes(java.util.List<Ingredient> ingredientes) {
        // Verifica que la lista de ingredientes no sea nula
        if (ingredientes == null || ingredientes.isEmpty()) {
            return new String[0]; // Devuelve un arreglo vacío si la lista está vacía
        }
        
        // Convierte la lista de ingredientes en un arreglo de nombres
        return ingredientes.stream()
                .map(Ingredient::getIngredientName) // Extrae el nombre de cada ingrediente
                .toArray(String[]::new); // Convierte a un arreglo
    }



}


