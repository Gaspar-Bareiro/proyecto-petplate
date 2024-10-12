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
public class RequestTokenAndUserNameDTO {
    private String token;
    private String userName;
}
