package com.proyecto_petplate.petplate.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto_petplate.petplate.DTO.RecipeRequestCreateDTO;
import com.proyecto_petplate.petplate.DTO.ingredientDTO;
import com.proyecto_petplate.petplate.Entities.EnumUnidadMedida;
import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;
import com.proyecto_petplate.petplate.Repositories.CategoryRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeIngredientRelationshipRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;


@Service
public class RecipeService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UploadFilesService uploadFilesService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private  RecipeIngredientRelationshipRepository recipeIngredientRelationshipRepo;


    public ResponseEntity<?> crearReceta(RecipeRequestCreateDTO receta){

        
        //validaciones del token
        //el token es de una sesion valida
        if (!jwtService.isTokenValid(receta.getToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }
        //validaciones del titulo
        //el titulo debe tener entre 5 y 200 caracteres
        if (receta.getTitle().length() > 200 || receta.getTitle().length() < 5) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("el titulo debe tener entre 5 y 200 caracteres"); //422
        }
        //validaciones de la descripcion
        // la descripcion debe no ser nula
        if (receta.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("la descripcion debe no ser nula"); //422
        }
        // la descripcion debe tener como minimo 100 caracteres
        if ( receta.getDescription().length() < 100) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("la descripcion debe tener al menos 100 caracteres"); //422
        }
        //verificaciones de animal tipo, verifica que si la combinacion de categoria / subcategoria existe en la db
        if (!categoryRepo.existsByCategoryNameAndSubcategoryName(receta.getCategoryName(),receta.getSubcategoryName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("la categoria no existe"); //422
        }

        //verificar ingredientes-------------------------------------------------------------------------
        if (receta.getIngredientes().length == 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no hay ingredientes"); //422
        }
        
        //obtiene una array de todos los nombres de ingredientes para facilitar la verificacion
        String[] ingredientesDBArray = ingredientService.obtenerNombresIngredientes(); // Convertir a List para búsqueda
        // Verificar todos los ingredientes
        for (ingredientDTO ingredientesReceta : receta.getIngredientes()) {
            // Comprobar si el ingredienteReceta existe en ingredientesDBArray
            if (!java.util.Arrays.asList(ingredientesDBArray).contains(ingredientesReceta.getName())) {
                // En caso de que un elemento no esté en la base de datos, suelta un error
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El ingrediente " + ingredientesReceta.getName() + " no existe"); // 409
            }

            // Verificar que la cantidad del ingrediente no sea 0 o menor
            if (ingredientesReceta.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Cantidad del ingrediente " + ingredientesReceta.getName() + " no es válida"); // 422
            }

            // Verificar que la unidad de medida esté permitida
            if (!EnumUnidadMedida.isValidUnit(ingredientesReceta.getUnitOfMeasurement())) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Unidad de medida del ingrediente " + ingredientesReceta.getName() + " no es válida"); // 422
            }
        }
        //validaciones de la imagen ------------------------------------------------------------------------

        MultipartFile img =  receta.getImg();
        String imgOriginalName = img.getOriginalFilename();

        long imgSize = img.getSize();
        long maxImgSize = 5 * 1024 * 1024;

        //valida que la imagen no tenga mas de 5MB
        if (imgSize > maxImgSize) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("el tamanio maximo del archivo debe ser de 5MB"); //422
        }
        //valida que tenga una extencion valida
        if (imgOriginalName != null) {
            if (!imgOriginalName.endsWith(".jpg") &&
            !imgOriginalName.endsWith(".jpeg") &&
            !imgOriginalName.endsWith(".png")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("el archivo tiene que tener la extencion .jpg .jpeg o .png"); //422
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se pudo obtener el nombre del archivo"); //422
        }

        // Si todas las validaciones pasan, retorna una respuesta exitosa

        String ubicacionImg = uploadFilesService.subirImgReceta(img);

        Recipe nuevaReceta = Recipe.builder()
            .recipeTitle(receta.getTitle())
            .recipeDescription(receta.getDescription())
            .recipeCreatedDate(new Date())
            .recipeUser(userRepo.getUserByUserName(jwtService.getUsernameFromToken(receta.getToken())))
            .recipeCategory(categoryRepo.findByCategoryNameAndSubcategoryName(receta.getCategoryName(), receta.getSubcategoryName()))
            .recipeImg(ubicacionImg)
            .build();

        recipeRepo.save(nuevaReceta);
        java.util.List<RecipeIngredientRelationship> listaRelaciones = new java.util.ArrayList<>();

        for (ingredientDTO ingrediente : receta.getIngredientes()) {
            RecipeIngredientRelationship newRelation = RecipeIngredientRelationship.builder()
                .quantity(ingrediente.getQuantity())
                .unitOfMeasurement(ingrediente.getUnitOfMeasurement())
                .ingredient(ingredientService.obtenerIngredientePorNombre(ingrediente.getName()))
                .recipe(nuevaReceta)
                .build();
            listaRelaciones.add(newRelation);
        }

        recipeIngredientRelationshipRepo.saveAll(listaRelaciones);




        return ResponseEntity.status(HttpStatus.OK).body("OK"); //422;
    }

}
