package com.proyecto_petplate.petplate.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ingredientDTO {
    private String name;
    private double quantity;
    private String unitOfMeasurement;
}
