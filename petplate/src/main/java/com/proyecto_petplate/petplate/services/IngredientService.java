package com.proyecto_petplate.petplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.Ingredient;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.IngredientRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;

import java.util.Arrays;



@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

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

    public ResponseEntity<?> crearIngrediente(String token, String ingredientName) {
        
        //verifica si el token es valido
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //obtiene el usuario del token
        User userToken = userRepo.getUserByUserName(jwtService.getUsernameFromToken(token));

        //verifica que tenga los permisos nesesarios 
        if (!userToken.getUserRol().getRolName().equals(EnumRolName.Administrador)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario del token no es un Administrador"); //401
        }

        //verifica que el nombre del ingrediente no tenga ente 3 y 30 caracteres
        if (ingredientName.length() > 30 || ingredientName.length() < 3) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("El nombre del ingrediente debe tener entre 3 y 30 caracteres"); //422
        }
        //le saca los espacios al pricipio y al final del String
        String ingrediente = ingredientName.trim();
        //pasa el primer caracter a mayuculas y los demas a minusculas
        ingrediente = ingrediente.substring(0, 1).toUpperCase() + ingrediente.substring(1).toLowerCase();

        //verifica que el ingrediente no exista en la base de datos
        if (java.util.Arrays.asList(obtenerNombresIngredientes()).contains(ingrediente)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El ingrediente ya existe en la base de datos"); //409
        }

        //crea el ingrediente y lo guarda el ingrediente en la base de datos
        ingredientRepo.save(Ingredient.builder().ingredientName(ingredientName).build());


        //actualiza la lista y el areglo de ingredientes
        refreshIngredientArray();

        //se creo el ingrediente con exito
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }



}


