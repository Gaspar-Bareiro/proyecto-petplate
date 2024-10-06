package com.proyecto_petplate.petplate.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeRequestCreateDTO {
    private String token;
    private String title;
    private String description;
    private MultipartFile img;
    private String categoryName;
    private String subcategoryName;
    private IngredientDTO[] ingredientes;
}

