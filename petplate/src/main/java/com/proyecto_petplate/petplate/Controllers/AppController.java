package com.proyecto_petplate.petplate.Controllers;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto_petplate.petplate.DTO.IngredientDTO;
import com.proyecto_petplate.petplate.DTO.RecipeBasicDataDTO;
import com.proyecto_petplate.petplate.DTO.RecipeResponseSearchDTO;
import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.RecipeIngredientRelationshipRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;
import com.proyecto_petplate.petplate.services.IngredientService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;





@Controller
@RequiredArgsConstructor
public class AppController {

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RecipeIngredientRelationshipRepository recipeIngredientRelationshipRepo;

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/recipe/{recipeId}")
    private String paginaReceta(@PathVariable int recipeId,Model model){
        //obtien la receta si existe
        java.util.Optional<Recipe> recetaOpcinal = recipeRepo.findById(recipeId);
        //verifica que exista la receta
        if (!recetaOpcinal.isEmpty()) {
            Recipe receta = recetaOpcinal.get();
            //agrego al modelo las caracteristicas basicas de la receta
            model.addAttribute("recipeUserId",receta.getRecipeUser().getUserId());
            model.addAttribute("titulo",receta.getRecipeTitle());
            model.addAttribute("usuario", receta.getRecipeUser().getUserName());
            model.addAttribute("descripcion", receta.getRecipeDescription());
            model.addAttribute("imgCategoria", receta.getRecipeCategory().getCategoryName()  + "-avatar.png");
            //si no exite la subcategoria
            if (receta.getRecipeCategory().getSubcategoryName() == null) {
                //agrego al modelo solo la categoria
                model.addAttribute("categoria", receta.getRecipeCategory().getCategoryName());
            } else { // sino 
                //agrego al modelo la categoria y sub categoria
                model.addAttribute("categoria", receta.getRecipeCategory().getCategoryName() +" - "+ receta.getRecipeCategory().getSubcategoryName());
            }
            //verifico si la receta cuenta con una imagen
            if (receta.getRecipeImg() != null) {
                //si es asi agrego la imagen al modelo
                model.addAttribute("recipeImg", receta.getRecipeImg());
            }
            //obtengo todos los ingredientes de la receta como Optional 
            java.util.Optional<java.util.List<RecipeIngredientRelationship>> ingredienteOptional = recipeIngredientRelationshipRepo.findRecipeIngredientRelationshipsByRecipeId(recipeId);
            //paso los ingrediente a java.util.List<RecipeIngredientRelationship>
            java.util.List<RecipeIngredientRelationship> ingredientes = ingredienteOptional.get();

            // Crear una lista para almacenar los strings de ingredientes
            java.util.List<String> ingredientesStrings = new java.util.ArrayList<>();
            
            // Recorrer la lista de ingredientes y crear los strings
            for (RecipeIngredientRelationship ingrediente : ingredientes) { //para cada ingrediente creo un string conformado por 
                String ingredienteString = String.format("%s %.2f %s", // "{nombre ingrediente} {cantidad}{unidadMedida}"
                    ingrediente.getIngredient().getIngredientName(), 
                    ingrediente.getQuantity(), 
                    ingrediente.getUnitOfMeasurement());
                // Agregar el string a la lista
                ingredientesStrings.add(ingredienteString);
            }
            //luego agrego una lista con todos los ingredientes al modelo
            model.addAttribute("ingredientes", ingredientesStrings);

            
            

            return "receta";
        } else{
            return "error404";
        }
        

        

    }

    @GetMapping("/profile/{profileId}")
    public String paginaPerfil(@PathVariable int profileId,Model model) {
        //obtiene el usuario si existe
        java.util.Optional<User> usuarioOptional = userRepo.findById(profileId);
        //si existe el usuario
        if (usuarioOptional.isPresent()) {
            //obtiene el usuario existete
            User usuario = usuarioOptional.get();
            //agrega al modelo el nombre de usuario
            model.addAttribute("userName",usuario.getUserName());

            String img = usuario.getUserImg();            
            //si tiene una imagen de perfil la agrega al modelo
            if (img != null) {
                model.addAttribute("userImgName", "/user-pictures/"+img);
            }

            //obtiene todas las recetas de ese usuario
            java.util.Optional<java.util.List<Recipe>> recetasOpcionales = recipeRepo.findByUser(usuario);

            //si tiene alguna receta
            if (recetasOpcionales.isPresent()) {
                //obtiene las recetas existentes
                java.util.List<Recipe> recetasExistentes = recetasOpcionales.get();
                // Convertir la lista de Recipe a RecipeResponseSearchDTO
                java.util.List<RecipeResponseSearchDTO> datosBasicosRecetas = recetasExistentes.stream()
                    .map(receta -> 
                        RecipeResponseSearchDTO.builder()
                            .recipeId(receta.getRecipeId())   // Mapea el ID de la receta
                            .title(receta.getRecipeTitle())   // Mapea el título de la receta
                            .Score(receta.getRecipeScore())   // Mapea el puntaje de la receta
                            // Mapea los ingredientes de la receta a un array de String
                            .ingredientes(ingredientService.obtenerNombresDeIngredientes(recipeIngredientRelationshipRepo.findIngredientsByRecipeId(receta.getRecipeId())))
                            .build() // Cierra el builder aquí
                    ) // Termina el mapeo aquí
                    .collect(java.util.stream.Collectors.toList()); // Recolecta la lista aquí
                
                //agrega los datos basicos de todas las recetas al modelo
                model.addAttribute("recetas", datosBasicosRecetas);
            }

            return "perfil";

        }
        return "error404";
    }
    
    @GetMapping("/")
    public String paginaPrinsipal(Model model) {
        
        // Obtén la fecha hace una semana
        java.util.Date fechaHaceUnaSemana = java.util.Date.from(Instant.now().minus(7, ChronoUnit.DAYS));

        // Inicialización de la lista vacía
        java.util.List<Recipe> listaRecetas = new ArrayList<>();

        // Consultas para obtener las mejores recetas por categoría
        String[] categorias = {"Perro", "Gato", "Conejo", "Tortuga", "Ave"};

        for (String categoria : categorias) {
            java.util.List<Recipe> recetas = recipeRepo.findTopRecipeByCategory(categoria, fechaHaceUnaSemana);
            if (!recetas.isEmpty()) {
                listaRecetas.add(recetas.get(0)); // Añade la mejor receta de la categoría
            }
        }

        if (!listaRecetas.isEmpty()) {

            java.util.List<RecipeBasicDataDTO> mejoresRecetasDatosBasicos = listaRecetas.stream()
                .map(receta -> 
                    RecipeBasicDataDTO.builder()
                        .recipeId(receta.getRecipeId())   // Mapea el ID de la receta
                        .title(receta.getRecipeTitle())   // Mapea el título de la receta
                        .Score(receta.getRecipeScore())   // Mapea el puntaje de la receta
                        .imgCategoria("/img/" + receta.getRecipeCategory().getCategoryName() + "-avatar.png")
                        // Mapea los ingredientes de la receta a un array de String
                        .ingredientes(ingredientService.obtenerNombresDeIngredientes(recipeIngredientRelationshipRepo.findIngredientsByRecipeId(receta.getRecipeId())))
                        .build() // Cierra el builder aquí
                ) // Termina el mapeo aquí
                .collect(java.util.stream.Collectors.toList()); // Recolecta la lista aquí
            
            model.addAttribute("mejoresRecetasDatosBasicos", mejoresRecetasDatosBasicos);
        }

        return "main";
    }
    
    @GetMapping("/recipe/create")
    public String paginaCrearReceta() {
        return "crearReceta";
    }

    @GetMapping("/recipe/modify/{recipeId}")
    public String modificarReceta(@PathVariable int recipeId,Model model) {


        //obtien la receta si existe
        java.util.Optional<Recipe> recetaOpcinal = recipeRepo.findById(recipeId);
        //verifica que exista la receta
        if (recetaOpcinal.isPresent()) {
            Recipe receta = recetaOpcinal.get();
            //agrego al modelo las caracteristicas basicas de la receta
            model.addAttribute("titulo",receta.getRecipeTitle());
            model.addAttribute("descripcion", receta.getRecipeDescription());
            model.addAttribute("categoria", receta.getRecipeCategory().getCategoryName());


            if (receta.getRecipeCategory().getSubcategoryName() != null) {
                model.addAttribute("subcategoria", receta.getRecipeCategory().getSubcategoryName());
            }

            
            //verifico si la receta cuenta con una imagen
            if (receta.getRecipeImg() != null) {
                //si es asi agrego la imagen al modelo
                model.addAttribute("recipeImg","/recipe-pictures/"+ receta.getRecipeImg());
            }
            
            
            //obtengo todos los ingredientes de la receta como Optional 
            java.util.Optional<java.util.List<RecipeIngredientRelationship>> relationshipOptional = recipeIngredientRelationshipRepo.findRecipeIngredientRelationshipsByRecipeId(recipeId);

            // Verificar si existe la lista
            if (relationshipOptional.isPresent()) {
                //paso los ingrediente a java.util.List<RecipeIngredientRelationship>
                java.util.List<RecipeIngredientRelationship> relationship = relationshipOptional.get();


                // Convertir la lista de RecipeIngredientRelationship a List<IngredientDTO>
                java.util.List<IngredientDTO> ingredientesDTO = relationship.stream()
                .map(this::mapToIngredientDTO) // Llama al método que mapea cada relación a IngredientDTO
                .collect(Collectors.toList());



                model.addAttribute("ingredientes", ingredientesDTO);
            }
            
        }
        return "modificarReceta";

    }

    // Método para convertir RecipeIngredientRelationship a IngredientDTO
    private IngredientDTO mapToIngredientDTO(RecipeIngredientRelationship relationship) {
        return IngredientDTO.builder()
                .name(relationship.getIngredient().getIngredientName())
                .quantity(relationship.getQuantity())
                .unitOfMeasurement(relationship.getUnitOfMeasurement())
                .build();
    }
    
    @GetMapping("/busqueda/resultados")
    public String resultadosBusqueda() {
        return "resultadosBusqueda";
    }
    

}
