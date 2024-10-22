package com.proyecto_petplate.petplate.services;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto_petplate.petplate.DTO.RecipeRequestCreateDTO;
import com.proyecto_petplate.petplate.DTO.RecipeRequestModifyDTO;
import com.proyecto_petplate.petplate.DTO.RecipeRequestSearchDTO;
import com.proyecto_petplate.petplate.DTO.RecipeResponseSearchDTO;
import com.proyecto_petplate.petplate.DTO.IngredientDTO;
import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.EnumUnidadMedida;
import com.proyecto_petplate.petplate.Entities.Ingredient;
import com.proyecto_petplate.petplate.Entities.Recipe;
import com.proyecto_petplate.petplate.Entities.RecipeDeleted;
import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationship;
import com.proyecto_petplate.petplate.Entities.RecipeIngredientRelationshipDeleted;
import com.proyecto_petplate.petplate.Entities.Recommendation;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.CategoryRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeDeletedRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeIngredientRelationshipDeletedRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeIngredientRelationshipRepository;
import com.proyecto_petplate.petplate.Repositories.RecipeRepository;
import com.proyecto_petplate.petplate.Repositories.RecommendationRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;

import jakarta.transaction.Transactional;



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
    private RecipeIngredientRelationshipRepository recipeIngredientRelationshipRepo;

    @Autowired
    private RecipeIngredientRelationshipDeletedRepository recipeIngredientRelationshipDeletedRepo;

    @Autowired
    private RecipeDeletedRepository recipeDeletedRepo;

    @Autowired
    private RecommendationRepository recommendationRepo;

    //metodo para crear una receta
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("API: la categoria no existe"); //409
        }

        //verificar ingredientes-------------------------------------------------------------------------
        if (receta.getIngredientes().length == 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no hay ingredientes"); //422
        }
        
        //obtiene una array de todos los nombres de ingredientes para facilitar la verificacion
        String[] ingredientesDBArray = ingredientService.obtenerNombresIngredientes(); // Convertir a List para búsqueda
        // Verificar todos los ingredientes
        for (IngredientDTO ingredientesReceta : receta.getIngredientes()) {
            // Comprobar si el ingredienteReceta existe en ingredientesDBArray
            if (!java.util.Arrays.asList(ingredientesDBArray).contains(ingredientesReceta.getName())) {
                // En caso de que un elemento no esté en la base de datos, suelta un error
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("API:El ingrediente " + ingredientesReceta.getName() + " no existe"); // 409
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

        User userReceta = userRepo.getUserByUserName(jwtService.getUsernameFromToken(receta.getToken()));
        //verifica que el usuario no tenga una receta con ese nombre
        if (recipeRepo.existsByTitleAndUser(receta.getTitle(), userReceta)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("API:El usuario ya publico una receta con el mismo titulo."); // 409
        }
        
        //validaciones de la imagen ------------------------------------------------------------------------

        String ubicacionImg = null;

        if (!receta.getImg().isEmpty()) {
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
            ubicacionImg = uploadFilesService.subirImgReceta(img);

        }

        // Si todas las validaciones pasan, retorna una respuesta exitosa
        Recipe nuevaReceta = Recipe.builder()
            .recipeTitle(receta.getTitle())
            .recipeDescription(receta.getDescription())
            .recipeCreatedDate(new Date())
            .recipeUser(userReceta)
            .recipeCategory(categoryRepo.findByCategoryNameAndSubcategoryName(receta.getCategoryName(), receta.getSubcategoryName()))
            .recipeImg(ubicacionImg)
            .build();

        recipeRepo.save(nuevaReceta);
        java.util.List<RecipeIngredientRelationship> listaRelaciones = new java.util.ArrayList<>();

        for (IngredientDTO ingrediente : receta.getIngredientes()) {
            RecipeIngredientRelationship newRelation = RecipeIngredientRelationship.builder()
                .quantity(ingrediente.getQuantity())
                .unitOfMeasurement(ingrediente.getUnitOfMeasurement())
                .ingredient(ingredientService.obtenerIngredientePorNombre(ingrediente.getName()))
                .recipe(nuevaReceta)
                .build();
            listaRelaciones.add(newRelation);
        }

        recipeIngredientRelationshipRepo.saveAll(listaRelaciones);




        return ResponseEntity.status(HttpStatus.OK).body("OK"); //200;
    }

    //metodo para modificar una receta
    public ResponseEntity<?> modificarReceta(RecipeRequestModifyDTO receta){
        java.util.Optional<Recipe> recetaAmodificar = recipeRepo.findById(receta.getRecipeId());
        if (recetaAmodificar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("No exista la receta con id =  " + receta.getRecipeId()); // 409
        }


        // Obtener la receta existente
        Recipe recetaExistente = recetaAmodificar.get();

        //valida si el token es valido
        if (!jwtService.isTokenValid(receta.getToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //valida si la receta le pertenece al usuario del taken
        if (userRepo.getUserByUserName(jwtService.getUsernameFromToken(receta.getToken())) != recetaExistente.getRecipeUser()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API:No te pertenece esta receta"); //401
        }

        // Validar y modificar el título
        if (!receta.getTitle().equals(recetaExistente.getRecipeTitle())) {
            if (receta.getTitle() != null && !receta.getTitle().trim().isEmpty()) {
            //si la nombre de la receta no tiene entre 5 y 200 caracteres suelta error
            if (receta.getTitle().length() > 200 || receta.getTitle().length() < 5) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("el titulo debe tener entre 5 y 200 caracteres"); //422
            }
            //si la el usuario tiene otra receta con el mismo nombre
            if (recipeRepo.existsByTitleAndUser(receta.getTitle(), recetaExistente.getRecipeUser())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("API:El usuario ya publico una receta con el mismo titulo."); // 409
            }
            recetaExistente.setRecipeTitle(receta.getTitle());
            }
        }
        

        // Validar y modificar la descripción
        if (receta.getDescription() != null && !receta.getDescription().trim().isEmpty()) {
            //verifica que la descripcion tenga mas de 100 caracteres
            if (receta.getDescription().length() < 100) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("la descripcion debe tener al menos 100 caracteres"); //422
            }
            recetaExistente.setRecipeDescription(receta.getDescription());

        }


        // Validar y modificar la categoría
        boolean isCategoryModified = receta.getCategoryName() != null && !receta.getCategoryName().trim().isEmpty();
        boolean isSubcategoryModified = receta.getSubcategoryName() != null && !receta.getSubcategoryName().trim().isEmpty();

        //if else anidado que ahora solo dios lo puede leer sin perderse y entenderlo
        //si se modifica la categoria
        if (isCategoryModified) {
            //si se modifica la categoria y subcategoria
            if (isSubcategoryModified) {
                //si la combinacion nueva categoria y sub categoria no exite
                if (!categoryRepo.existsByCategoryNameAndSubcategoryName(receta.getCategoryName(),receta.getSubcategoryName())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).
                        body("la categoria "+receta.getCategoryName()+"-"+receta.getSubcategoryName()+" no existe"); //422
                }else{//la combinacion existe categoria/subcategoria existe
                    //guarda la combinacion
                    recetaExistente.setRecipeCategory(categoryRepo.findByCategoryNameAndSubcategoryName(receta.getCategoryName(),receta.getSubcategoryName()));
                }
            }else{ // solo se modifico la categoria
                //si la combinacion categoria con sub categoria existente no existe
                if (!categoryRepo.existsByCategoryNameAndSubcategoryName(receta.getCategoryName(),recetaExistente.getRecipeCategory().getSubcategoryName())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).
                        body("la categoria "+receta.getCategoryName()+"-"+recetaExistente.getRecipeCategory().getSubcategoryName()+" no existe"); //422
                }else{  // si la combinacion categoria con sub categoria existente si existe
                    //guarda la combinacion
                    recetaExistente.setRecipeCategory(categoryRepo
                        .findByCategoryNameAndSubcategoryName(receta.getCategoryName(),recetaExistente.getRecipeCategory().getSubcategoryName()));
                }
            }
        }else{// si no se modifico la categoria 
            //vertifica si se modifico la subcategoria
            if (isSubcategoryModified){
                //verifica si la combinacion categoria existente y nueva sub categoria existe
                if (!categoryRepo.existsByCategoryNameAndSubcategoryName(recetaExistente.getRecipeCategory().getCategoryName(), receta.getSubcategoryName())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).
                        body("la categoria "+recetaExistente.getRecipeCategory().getCategoryName()+"-"+receta.getSubcategoryName()+" no existe"); //422
                }else{// si la combinacion existe
                    //guarda la combinacion
                    recetaExistente.setRecipeCategory(categoryRepo
                        .findByCategoryNameAndSubcategoryName(recetaExistente.getRecipeCategory().getCategoryName(),receta.getSubcategoryName()));
                }
            }
        }

        

        // Validar y modificar ingredientes
        if (receta.getIngredientes() != null && receta.getIngredientes().length > 0) {
            //obtiene una array de todos los nombres de ingredientes para facilitar la verificacion
            String[] ingredientesDBArray = ingredientService.obtenerNombresIngredientes(); // Convertir a List para búsqueda
            // Verificar todos los ingredientes
            for (IngredientDTO ingredientesReceta : receta.getIngredientes()) {
                // Comprobar si el ingredienteReceta existe en ingredientesDBArray
                if (!java.util.Arrays.asList(ingredientesDBArray).contains(ingredientesReceta.getName())) {
                    // En caso de que un elemento no esté en la base de datos, suelta un error
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("API:El ingrediente " + ingredientesReceta.getName() + " no existe"); // 409
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
        }

        // Validar y modificar la imagen
        if (receta.getImg() != null && !receta.getImg().isEmpty()) {

            String ubicacionImg = null;

            MultipartFile img =  receta.getImg();
            String imgOriginalName = img.getOriginalFilename();

            long imgSize = img.getSize();
            long maxImgSize = 5 * 1024 * 1024;

            //valida que la imagen no tenga mas de 5MB
            if (imgSize > maxImgSize) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("el tamaño maximo del archivo debe ser de 5MB"); //422
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

            //elimina la imagen anterior
            uploadFilesService.deleteImgReceta(recetaExistente.getRecipeImg());
            //sube la nueva imagen
            ubicacionImg = uploadFilesService.subirImgReceta(img);
            //define la nueva imagen
            recetaExistente.setRecipeImg(ubicacionImg); 

        }
        //paso todas las verificaciones

        // Guardar la receta modificada en el repositorio
        recipeRepo.save(recetaExistente);

        //si se modificaron las relaciones receta-ingrediente
        if (receta.getIngredientes() != null && receta.getIngredientes().length > 0){
            //elimina las relaciones viejas
            recipeIngredientRelationshipRepo.deleteByRecipeId(recetaExistente.getRecipeId());
            //obtiene las nuevas relaciones
            java.util.List<RecipeIngredientRelationship> listaRelaciones = new java.util.ArrayList<>();

            for (IngredientDTO ingrediente : receta.getIngredientes()) {
                RecipeIngredientRelationship newRelation = RecipeIngredientRelationship.builder()
                    .quantity(ingrediente.getQuantity())
                    .unitOfMeasurement(ingrediente.getUnitOfMeasurement())
                    .ingredient(ingredientService.obtenerIngredientePorNombre(ingrediente.getName()))
                    .recipe(recetaExistente)
                    .build();
                listaRelaciones.add(newRelation);
            }

            //guarda las nuevas relaciones 
            recipeIngredientRelationshipRepo.saveAll(listaRelaciones);
        }

        


        return ResponseEntity.ok("Receta modificada exitosamente.");

    }

    //metodo para buscar receta
    public ResponseEntity<?> buscarReceta(RecipeRequestSearchDTO busqueda) {


        boolean categoryIsValid = !(busqueda.getCategoryName() == null || busqueda.getCategoryName().isEmpty());
        
        boolean subcategoryIsValid = !(busqueda.getSubcategoryName() == null || busqueda.getSubcategoryName().isEmpty());

        boolean ingredientesIsValid = !(busqueda.getIngredientes() == null || busqueda.getIngredientes().length == 0);

        //si no se seleciono ningun parametro para la busqueda
        if (!categoryIsValid && !subcategoryIsValid && !ingredientesIsValid) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono ningun parametro para la busqueda"); //422
        }

        //verifica si se busco por subcategoria sin haber especificado la categoria
        if (subcategoryIsValid && !categoryIsValid) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Se seleciono una subcategoria pero no una categoria"); //422
        }

        //si se busca por categoria
        if (categoryIsValid) {
            // verifica que si la combinacion de categoria / subcategoria existe en la db
            if (subcategoryIsValid) {
                if (!categoryRepo.existsByCategoryNameAndSubcategoryName(busqueda.getCategoryName(),busqueda.getSubcategoryName())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("la categoria no existe"); //409
                }
            }else{
                if (!categoryRepo.existsByCategoryNameAndSubcategoryName(busqueda.getCategoryName(),null)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("la categoria no existe"); //409
                }
            }
            
        }

        
        // Si hay ingredientes, verifica que estos sean válidos
        if (ingredientesIsValid) {
            // Obtiene una lista de todos los nombres de ingredientes para facilitar la verificación
            String[] ingredientesDBArray = ingredientService.obtenerNombresIngredientes();
            
            // Convertir el array a un Set para búsquedas rápidas
            java.util.Set<String> ingredientesDBSet = new java.util.HashSet<>(java.util.Arrays.asList(ingredientesDBArray));
            
            // Verificar todos los ingredientes
            for (String ingredienteBusqueda : busqueda.getIngredientes()) {
                // Comprobar si el ingredienteReceta existe en ingredientesDBSet
                if (!ingredientesDBSet.contains(ingredienteBusqueda)) {
                    // En caso de que un elemento no esté en la base de datos, suelta un error
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("el ingrediente "+ ingredienteBusqueda +" no existe"); //409
                }
            }
        }


        // Lista de recetas resultante
        java.util.Optional<java.util.List<Recipe>> recetas = java.util.Optional.of(new java.util.ArrayList<>());
        
        java.util.List<Ingredient> ingredientesList;
        

        // Estructura anidada para verificar el caso y obtener las recetas correspondientes
        if (!ingredientesIsValid && categoryIsValid && !subcategoryIsValid) {// Solo categoría
            recetas = recipeRepo.findByCategoryName(busqueda.getCategoryName()); //obtiene las recetas matcheadas
        } else if (categoryIsValid && subcategoryIsValid && !ingredientesIsValid) { // categoria y subcategoria
            recetas = recipeRepo.findByCategoryAndSubCategory(busqueda.getCategoryName(), busqueda.getSubcategoryName());//obtiene las recetas matcheadas
        } else if (!categoryIsValid && !subcategoryIsValid && ingredientesIsValid) {//solo ingredientes
            ingredientesList = ingredientService.obtenerListaIngredientesPorNombres(busqueda.getIngredientes()); //como se obtiene la lista de ingredientes
            recetas = recipeIngredientRelationshipRepo.findRecipesByAllIngredients(ingredientesList,ingredientesList.size()); //obtiene las recetas matcheadas
        } else if (categoryIsValid && !subcategoryIsValid && ingredientesIsValid) {//categoria e ingredientes
            ingredientesList = ingredientService.obtenerListaIngredientesPorNombres(busqueda.getIngredientes()); //como se obtiene la lista de ingredientes
            recetas = intersectRecipes( //obtiene la interseccion entre 2 conjuntos de recetas con la clase java.util.Optional<java.util.List<Recipe>>
                recipeRepo.findByCategoryName(busqueda.getCategoryName()), //obtiene las recetas por categoria 
                recipeIngredientRelationshipRepo.findRecipesByAllIngredients(ingredientesList,ingredientesList.size()));//obtien las recetas por ingredientes
        } else if (categoryIsValid && subcategoryIsValid && ingredientesIsValid) { // categoria, subcategoria e ingredientes
            ingredientesList = ingredientService.obtenerListaIngredientesPorNombres(busqueda.getIngredientes()); //como se obtiene la lista de ingredientes
            recetas = intersectRecipes( //obtiene la interseccion entre 2 conjuntos de recetas con la clase java.util.Optional<java.util.List<Recipe>>
                recipeRepo.findByCategoryAndSubCategory(busqueda.getCategoryName(), busqueda.getSubcategoryName()), //obtiene las recetas por categoria y sub categoria
                recipeIngredientRelationshipRepo.findRecipesByAllIngredients(ingredientesList,ingredientesList.size()));//obtien las recetas por ingredientes
        }

        if (recetas.isEmpty() || recetas.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("No hay receta que cumpla los requisitos de busqueda"); // 409
        }else{
            
            // Obtener la lista de recetas existente
            java.util.List<Recipe> recetasExistentes = recetas.get();

            // Convertir la lista de Recipe a RecipeResponseSearchDTO
            java.util.List<RecipeResponseSearchDTO> response = recetasExistentes.stream()
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

            // Devolver la respuesta con la lista de RecipeResponseSearchDTO
            return ResponseEntity.ok(response);
        }
    }

    //metodo para borrar una receta
    @Transactional
    public ResponseEntity<?> borrarReceta(int recipeId, String token) {
        
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        User userToken = userRepo.getUserByUserName(jwtService.getUsernameFromToken(token));

        if (!userToken.getUserRol().getRolName().equals(EnumRolName.Auditor)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario tiene el rol " + userToken.getUserRol().getRolName() + " no el de Auditor"); //401
        }

        if (!recipeRepo.existsById(recipeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La receta no existe"); //409
        }

        try {
            // obtiene la receta
            Recipe receta = recipeRepo.findById(recipeId).get();
            
            // obtiene los ingredientes de la receta
            java.util.List<RecipeIngredientRelationship> relacion = recipeIngredientRelationshipRepo
                .findRecipeIngredientRelationshipsByRecipeId(receta.getRecipeId()).get();
            
            // guarda la receta en la tabla de recetas borradas
            recipeDeletedRepo.save(new RecipeDeleted(receta));
            
            // guarda las relaciones en las tablas de recetas borradas
            java.util.List<RecipeIngredientRelationshipDeleted> relacionDeleted = relacion.stream()
                .map(RecipeIngredientRelationshipDeleted::new)  // el constructor que recibe RecipeIngredientRelationship
                .collect(Collectors.toList());  // Convertimos el stream a una lista
            recipeIngredientRelationshipDeletedRepo.saveAll(relacionDeleted);

            //borra las recomendaciones de la receta
            recommendationRepo.deleteByRecommendationRecipe(receta);
            
            // borra los ingredientes de la receta
            recipeIngredientRelationshipRepo.deleteAll(relacion);
            
            // borra la receta
            recipeRepo.delete(receta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se pudo eliminar la receta " + e.getMessage()); //409
        }
        

        return ResponseEntity.status(HttpStatus.OK).body("OK"); //200;

    }

    //metodo para obtener la interseccion entre 2 conjuntos de recetas optional
    public java.util.Optional<java.util.List<Recipe>> intersectRecipes(java.util.Optional<java.util.List<Recipe>> recipes1, java.util.Optional<java.util.List<Recipe>> recipes2) {
    // Verifica si ambos Optional contienen valores
    if (recipes1.isPresent() && recipes2.isPresent()) {
        // Obtiene las listas de recetas
        java.util.List<Recipe> list1 = recipes1.get();
        java.util.List<Recipe> list2 = recipes2.get();
        
        // Realiza la intersección de ambas listas
        java.util.List<Recipe> intersection = list1.stream()
            .filter(list2::contains) // Filtra las recetas que están en list2
            .collect(java.util.stream.Collectors.toList()); // Recolecta el resultado en una nueva lista
        
        // Retorna el resultado como un Optional
        return java.util.Optional.of(intersection);
    }
    
    // Si alguno de los Optional está vacío, devuelve un Optional vacío
    return java.util.Optional.empty();
}

    //metodo para recomendar una receta
    public ResponseEntity<?> setRecommendation(int recipeId, String token) {
        

        //comprueba si la sesion es valida por ende tambien comprueba que el usuario es valido
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //comprueba si existe la receta
        if (!recipeRepo.existsById(recipeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("no existe una receta con esa id"); //409
        }

        // se obtiene ek usuario y la receta
        User user = userRepo.getUserByUserName(jwtService.getUsernameFromToken(token));
        Recipe receta = recipeRepo.findById(recipeId).get();

        // se usa el usuario y la receta para verificar si ese usuario no recomendo la receta
        if (recommendationRepo.existsByRecommendationUserAndRecommendationRecipe(user, receta)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("el usuario ya reecomendo la receta"); //409
        }

        Recommendation recomendacion = Recommendation.builder()
            .recommendationUser(user)
            .recommendationRecipe(receta)
            .build();

        //agrega la recomendacion y el triger aumenta el contador de recomendaciones de la receta
        recommendationRepo.save(recomendacion);

        return ResponseEntity.status(HttpStatus.OK).body("OK"); //200;
    }

    //metodo para sacar la recomendacion a una receta
    @Transactional
    public ResponseEntity<?> removeRecommendation(int recipeId, String token) {
        //comprueba si la sesion es valida por ende tambien comprueba que el usuario es valido
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //comprueba si existe la receta
        if (!recipeRepo.existsById(recipeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("no existe una receta con esa id"); //409
        }

        // se obtiene ek usuario y la receta
        User user = userRepo.getUserByUserName(jwtService.getUsernameFromToken(token));
        Recipe receta = recipeRepo.findById(recipeId).get();

        // se usa el usuario y la receta para verificar si ese usuario ya recomendo la receta
        if (!recommendationRepo.existsByRecommendationUserAndRecommendationRecipe(user, receta)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("el usuario no reecomendo la receta"); //409
        }

        

        //agrega la recomendacion y el triger aumenta el contador de recomendaciones de la receta
        recommendationRepo.deleteByRecommendationUserAndRecommendationRecipe(user, receta);

        return ResponseEntity.status(HttpStatus.OK).body("OK"); //200;
    } 

}
