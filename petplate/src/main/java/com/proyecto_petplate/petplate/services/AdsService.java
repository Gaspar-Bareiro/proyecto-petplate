package com.proyecto_petplate.petplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto_petplate.petplate.DTO.AdsRequestChangeImgDTO;
import com.proyecto_petplate.petplate.DTO.AdsRequestDeleteImgDTO;
import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.User;
import com.proyecto_petplate.petplate.Repositories.UserRepository;

@Service
public class AdsService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UploadFilesService uploadFilesService;
    
    public ResponseEntity<?> cambiarImagenAnuncios(AdsRequestChangeImgDTO data){
        
        if (!jwtService.isTokenValid(data.getToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //obtiene el usuario del token
        User userToken = userRepo.getUserByUserName(jwtService.getUsernameFromToken(data.getToken()));

        //verifica que tenga los permisos nesesarios 
        if (!userToken.getUserRol().getRolName().equals(EnumRolName.Administrador)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario del token no es un Administrador"); //401
        }
        if(data.getLocation() == null){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono el anuncio a cambiar"); //422
        }

        String location = data.getLocation().trim();
        if (!location.equals("left") && !location.equals("right") && !location.equals("both")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono el anuncio a cambiar"); //422
        }

        //valida la imagen --------------------------------------------------------------------------------------------------------
        MultipartFile img =  data.getImg();

        if (img == null || img.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono ninguna imagen"); //422
        }

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

        //si pasaron todas las validaciones
        //guarda la imagen del anuncio en el servidor
        uploadFilesService.deleteAnuncio(location);
        boolean nuevaImg = uploadFilesService.subirAnuncio(location,img);
        if (!nuevaImg) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Error al subir el archivo"); //422
        }

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }



    public ResponseEntity<?> EliminarAnuncios(AdsRequestDeleteImgDTO data){
        if (!jwtService.isTokenValid(data.getToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token de sesion invalido"); //401
        }

        //obtiene el usuario del token
        User userToken = userRepo.getUserByUserName(jwtService.getUsernameFromToken(data.getToken()));

        //verifica que tenga los permisos nesesarios 
        if (!userToken.getUserRol().getRolName().equals(EnumRolName.Administrador)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario del token no es un Administrador"); //401
        }
        if(data.getLocation() == null){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono el anuncio a cambiar"); //422
        }

        String location = data.getLocation().trim();
        if (!location.equals("left") && !location.equals("right") && !location.equals("both")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("no se selecciono el anuncio a cambiar"); //422
        }
        uploadFilesService.deleteAnuncio(data.getLocation());
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
