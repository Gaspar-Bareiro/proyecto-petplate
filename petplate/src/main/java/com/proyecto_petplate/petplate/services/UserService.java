package com.proyecto_petplate.petplate.services;

import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto_petplate.petplate.DTO.UserRequestLoginDTO;
import com.proyecto_petplate.petplate.DTO.UserRequestRegisterDTO;
import com.proyecto_petplate.petplate.DTO.UserResponseLoginDTO;
import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.Rol;
import com.proyecto_petplate.petplate.Entities.Session;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.RolRepository;
import com.proyecto_petplate.petplate.Repositories.SessionRepository;
import com.proyecto_petplate.petplate.Repositories.UserRepository;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RolRepository RolRepo;

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancia de BCryptPasswordEncoder

    public boolean validarContraseña(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public ResponseEntity<?> crearUsuario(UserRequestRegisterDTO usuario){

        String name = usuario.getUserName().trim();
        String email = usuario.getUserEmail().trim();
        String password = usuario.getUserPassword().trim();

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

        //verifica si el email ya esta en uso
        if (userRepo.existsByUserEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Email ya se encuentra en uso"); //409
        }

        //agrege verificaciones para que la password tenga minimo 8 caracteres y no contenga espacios
        //verifica que la password tenga entre 8 y 20 caracteres
        if (password.length() < 8 || password.length() > 20) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La contraseña debe tener de 8 a 20 caracteres"); //409
        }
        //verifica que la pasword no contenga espacios
        if (password.contains(" ")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La contraseña no puede contener espacios"); //409
        }


        //una vez pasadas todas las verificaciones crea un objeto usuario y lo inserta en la DB
        User usuarioFinal = new User();
        Rol rol = RolRepo.findByRolName(EnumRolName.Usuario);
        usuarioFinal.setUserName(name);
        usuarioFinal.setUserEmail(email);
        usuarioFinal.setUserImg(null);
        usuarioFinal.setUserRol(rol);
        // hashea y guarda la password
        String hashedPassword = passwordEncoder.encode(password);
        usuarioFinal.setUserPassword(hashedPassword); // guardarlo en hash
        //guarda el usuario en la DB
        userRepo.save(usuarioFinal);
        //Devuelve OK
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    public ResponseEntity<?> iniciarSesion(UserRequestLoginDTO usuario) {

        String name = usuario.getUser();
        String password = usuario.getPassword();

        //error de capa 8 , no tiene usuario y se puso a jugar con el formulario por lo cual no deberia
        //hacer ninguna consulta a la base de datos
        if (name.length() > 30 || name.contains(" ")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Usuario no exite."); //409
        }

        //verifica que exista un usuario con ese nombre utilizando la base de datos
        if (!userRepo.existsByUserName(name)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Usuario "+ name +" no exite."); //409
        }

        //verifica si la password es correcta utilizando la base de datos
        User userDB = userRepo.getUserByUserName(name);
        if (!validarContraseña(password, userDB.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Contraseña incorrecta."); //409
        }

        //paso las comprobaciones 

        //aca tengo  el tema del token 
        String token = jwtService.getToken(userDB.getUserName(),userDB.getUserId());//creo el token
        Date createdDate = jwtService.getCreationFromToken(token);//obtengo fecha de creacion
        Date expiredDate = jwtService.getExpirationFromToken(token);//obtengo la fecha de expiracion
        Session crearSesion = Session.builder()//contruyo la sesion
            .sesionToken(token)
            .createdDate(createdDate)
            .expiredDate(expiredDate)
            .sesionUser(userDB)
            .build();
        
        //guarda la sesion en la db
        sessionRepo.save(crearSesion);
        //creo la respuesta
        UserResponseLoginDTO response = new UserResponseLoginDTO(token);
        //envio la respuesta
        return ResponseEntity.ok(response);

    }
}
