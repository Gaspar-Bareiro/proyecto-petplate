package com.proyecto_petplate.petplate.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestLoginDTO {

    private String user;

    private String password;
    
}
