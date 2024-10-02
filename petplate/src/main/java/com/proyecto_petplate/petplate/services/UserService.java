package com.proyecto_petplate.petplate.services;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.DTO.UserRequestRegisterDTO;
import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.Rol;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.RolRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RolRepository RolRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancia de BCryptPasswordEncoder

    public boolean validarContraseña(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public ResponseEntity<?> crearUsuario(UserRequestRegisterDTO usuario){

        String name = usuario.getUserName().trim();
        String email = usuario.getUserEmail().trim();

        //verificaciones de userName --------------------------------------------------------------------------------------------
        //verifica que el nombre no contenga espacios
        if (name.contains(" ")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Nombre no puede contener espacios"); //409
        }
        //verifica que el nombre no tenga mas de 30 caracteres
        if (name.length() > 30) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Nombre no puede contener mas de 30 caracteres"); //409
        }

        //una vez verificados los casos menos exigentes se verifican los casos que requieren consultas a la base de datos
        //verifica que el usuario no exista
        if (userRepo.existsByUserName(name)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario en ya existente"); //409
        }

        //verificaciones de Email ----------------------------------------------------------------------------------------------

        //verifica que el Email no sea muy largo
        if (email.length() > 254) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Email no puede contener mas de 254 caracteres"); //409
        }

        //verifica que el email no conga espacios
        if (email.contains(" ")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Email no puede contener espacios"); //409
        }

        //esta clase sirve para verificar si es un Email valido
        EmailValidator validator = EmailValidator.getInstance();

        //verifica que sea un email valido
        if (!validator.isValid(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Email no cumple el formato"); //409
        }

        if (userRepo.existsByUserEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Email ya se encuentra en uso"); //409
        }

        //una vez pasadas todas las verificaciones crea un objeto usuario y lo inserta en la DB
        User usuarioFinal = new User();
        Rol rol = RolRepo.findByRolName(EnumRolName.Usuario);
        usuarioFinal.setUserName(name);
        usuarioFinal.setUserEmail(email);
        usuarioFinal.setUserImg(null);
        usuarioFinal.setUserRol(rol);
        // hashea y guarda la password
        String hashedPassword = passwordEncoder.encode(usuario.getUserPassword());
        usuarioFinal.setUserPassword(hashedPassword); // guardarlo en hash
        //guarda el usuario en la DB
        userRepo.save(usuarioFinal);
        //Devuelve OK
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
