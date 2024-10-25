package com.proyecto_petplate.petplate.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseLoginDTO {

    private String token;
    private int userId;
    private String userImg;
    private String userRol;
    
}
