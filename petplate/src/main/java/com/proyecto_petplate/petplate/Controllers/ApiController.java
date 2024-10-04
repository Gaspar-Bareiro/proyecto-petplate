package com.proyecto_petplate.petplate.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto_petplate.petplate.DTO.UserRequestLoginDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestRegisterDTO;
import com.proyecto_petplate.petplate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController // es un controlador de api
@RequestMapping("/apiv1") // ruta inicial
@RequiredArgsConstructor
public class ApiController {

    //inyeccion de dependencias (@autowired)
    @Autowired
    private UserService userService;
    
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
}
