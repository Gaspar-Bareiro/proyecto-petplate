package com.proyecto_petplate.petplate.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto_petplate.petplate.DTO.RecipeRequestCreateDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestLoginDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestRegisterDTO;
import com.proyecto_petplate.petplate.DTO.ingredientDTO;
import com.proyecto_petplate.petplate.services.RecipeService;
import com.proyecto_petplate.petplate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController // es un controlador de api
@RequestMapping("/apiv1") // ruta inicial
@RequiredArgsConstructor
public class ApiController {

    //inyeccion de dependencias (@autowired)
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;
    
    //servicio Register
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserRequestRegisterDTO usuario) {
        return userService.crearUsuario(usuario);
    }

    //login
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserRequestLoginDTO usuario){
        return userService.iniciarSesion(usuario);
    }

    @PostMapping(value = "/recipe/create", consumes = "multipart/form-data")
    public ResponseEntity<?> crearReceta(
        @RequestPart(value = "token", required = false) String token,
        @RequestPart(value = "title", required = false) String title,
        @RequestPart(value = "description", required = false) String description,
        @RequestPart(value = "img", required = false) MultipartFile img,
        @RequestPart(value = "categoryName", required = false) String categoryName,
        @RequestPart(value = "subcategoryName", required = false) String subcategoryName,
        @RequestPart(value = "ingredientes", required = false) ingredientDTO[] ingredientes) {
    
    // Manejar valores nulos si es necesario
    if (ingredientes == null || ingredientes.length == 0) {
        System.out.println("No se han enviado ingredientes.");
        ingredientes = new ingredientDTO[0];
    } else {
        System.out.println("Número de ingredientes: " + ingredientes.length);
        for (ingredientDTO ingrediente : ingredientes) {
            System.out.println("Nombre: " + ingrediente.getName());
            System.out.println("Cantidad: " + ingrediente.getQuantity());
            System.out.println("Unidad: " + ingrediente.getUnitOfMeasurement());
        }
    }

    // Crea un DTO con los datos recibidos
    RecipeRequestCreateDTO receta = RecipeRequestCreateDTO.builder()
            .token(token)
            .title(title)
            .description(description)
            .img(img)
            .categoryName(categoryName)
            .subcategoryName(subcategoryName)
            .ingredientes(ingredientes)
            .build();

    // Llama al servicio para crear la receta
    return recipeService.crearReceta(receta);
    }
    
}
