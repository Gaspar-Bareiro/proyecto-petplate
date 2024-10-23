package com.proyecto_petplate.petplate.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeResponseSearchDTO {
    private int recipeId;
    private String title;
    private int Score;
    private String category;
    private String[] ingredientes;
}
