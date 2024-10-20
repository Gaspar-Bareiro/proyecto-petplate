package com.proyecto_petplate.petplate.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto_petplate.petplate.DTO.RecipeRequestCreateDTO;
import com.proyecto_petplate.petplate.DTO.RecipeRequestModifyDTO;
import com.proyecto_petplate.petplate.DTO.RecipeRequestSearchDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestLoginDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestRegisterDTO;
import com.proyecto_petplate.petplate.services.IngredientService;
import com.proyecto_petplate.petplate.services.RecipeService;
import com.proyecto_petplate.petplate.services.UserService;
import com.proyecto_petplate.petplate.DTO.IngredientDTO;
import com.proyecto_petplate.petplate.DTO.RequestOnlyTokenDTO;
import com.proyecto_petplate.petplate.DTO.RequestTokenAndIngredientNameDTO;
import com.proyecto_petplate.petplate.DTO.RequestTokenAndUserNameDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Autowired
    private IngredientService ingredientService;
    
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

    //crear receta
    @PostMapping(value = "/recipe/create", consumes = "multipart/form-data")
    public ResponseEntity<?> crearReceta(
        @RequestPart(value = "token", required = false) String token,
        @RequestPart(value = "title", required = false) String title,
        @RequestPart(value = "description", required = false) String description,
        @RequestPart(value = "img", required = false) MultipartFile img,
        @RequestPart(value = "categoryName", required = false) String categoryName,
        @RequestPart(value = "subcategoryName", required = false) String subcategoryName,
        @RequestPart(value = "ingredientes", required = false) IngredientDTO[] ingredientes) {
    
    
    // Manejar valores nulos si es necesario
    if (ingredientes == null) {
        ingredientes = new IngredientDTO[0];
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

    //modificar receta
    @PutMapping(value = "/recipe/modify/{recipeId}", consumes = "multipart/form-data")
    public ResponseEntity<?> modificarReceta(
        @PathVariable int recipeId,  // Se obtiene desde el path
        @RequestPart(value = "token", required = false) String token,
        @RequestPart(value = "title", required = false) String title,
        @RequestPart(value = "description", required = false) String description,
        @RequestPart(value = "img", required = false) MultipartFile img,
        @RequestPart(value = "categoryName", required = false) String categoryName,
        @RequestPart(value = "subcategoryName", required = false) String subcategoryName,
        @RequestPart(value = "ingredientes", required = false) IngredientDTO[] ingredientes) {

        // Manejar valores nulos si es necesario
        if (ingredientes == null) {
            ingredientes = new IngredientDTO[0];
        }

        // Crea un DTO con los datos recibidos
        RecipeRequestModifyDTO receta = RecipeRequestModifyDTO.builder()
                .recipeId(recipeId)  // Ahora el ID se toma del Path
                .token(token)
                .title(title)
                .description(description)
                .img(img)
                .categoryName(categoryName)
                .subcategoryName(subcategoryName)
                .ingredientes(ingredientes)
                .build();

        // Llama al servicio para modificar
        return recipeService.modificarReceta(receta);
    }

    //buscar receta
    @PostMapping("/recipe/search")
    public ResponseEntity<?> buscarReceta(@RequestBody RecipeRequestSearchDTO busqueda) {
    return recipeService.buscarReceta(busqueda);
    }
    
    //obtener array de ingredientes
    @GetMapping("/ingredients")
    public ResponseEntity<?> obtenerArrayIngredientes() {
        return ResponseEntity.ok(ingredientService.obtenerNombresIngredientes());
    }

    //borra una receta
    @PostMapping("/recipe/delete/{recipeId}")
    public ResponseEntity<?> borrarReceta(@PathVariable int recipeId,@RequestBody RequestOnlyTokenDTO token){
        return recipeService.borrarReceta(recipeId , token.getToken());
    }

    //recomienda una receta
    @PostMapping("/recipe/addRecommendation/{recipeId}")
    public ResponseEntity<?> recomendarReceta(@PathVariable int recipeId,@RequestBody RequestOnlyTokenDTO token){
        return recipeService.setRecommendation(recipeId, token.getToken());
    }

    @PostMapping("/recipe/removeRecommendation/{recipeId}")
    public ResponseEntity<?> sacarRecomendacionReceta(@PathVariable int recipeId,@RequestBody RequestOnlyTokenDTO token) {
        return recipeService.removeRecommendation(recipeId, token.getToken());
    }

    @GetMapping("/back_office/auditors")
    public ResponseEntity<?> obtenerTodosLosAuditores(@RequestBody RequestOnlyTokenDTO token) {
        return userService.obtenerTodosLosAuditores(token.getToken());
    }
    
    @PostMapping("/back_office/auditors/create")
    public ResponseEntity<?> darRolAuditor(@RequestBody RequestTokenAndUserNameDTO request) {
        return userService.darRolAuditor(request.getToken(),request.getUserName());
    }

    @PostMapping("/back_office/auditors/remove")
    public ResponseEntity<?> sacarRolAuditor(@RequestBody RequestTokenAndUserNameDTO request) {
        return userService.sacarRolAuditor(request.getToken(),request.getUserName());
    }

    @PostMapping("/back_office/ingredients/create")
    public ResponseEntity<?> postMethodName(@RequestBody RequestTokenAndIngredientNameDTO request) {
        return ingredientService.crearIngrediente(request.getToken(),request.getIngredientName());
    }
    

    

}
