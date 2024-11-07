package com.proyecto_petplate.petplate.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Service
public class UploadFilesService {

    //guarda la imagen el el servidor y devuelve donde se guardo
    //en caso de no cumplir con condiciones no devuelve nada
    public String subirImgReceta(MultipartFile img){
        try {
            byte[] bytes = img.getBytes();
            String imgName = UUID.randomUUID().toString();

            
            String imgOriginalName = img.getOriginalFilename();
            if (imgOriginalName == null) {
                return "";
            }

            String imgExtencion = imgOriginalName.substring(imgOriginalName.lastIndexOf("."));

            String newImgName = imgName + imgExtencion;

            File folder = new File("recipe-pictures");
            if (!folder.exists()) {
                folder.mkdir();
            }

            Path path = Paths.get("recipe-pictures/" + newImgName); 

            Files.write(path, bytes);// crea el archibo

            return newImgName;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
        
    }

    //borrar imagen receta
    public void deleteImgReceta(String oldName){
        // Definir la ruta del archivo que se desea eliminar
        if (oldName != null && !oldName.trim().isEmpty()) {
            
            Path path = Paths.get("recipe-pictures/" + oldName);
            try {
                // Verificar si el archivo existe
                if (Files.exists(path)) {
                    // Eliminar el archivo
                    Files.delete(path);
                }
            } catch (Exception e) {
                System.out.println("Error al eliminar la imagen: " + e.getMessage());
            }
        }
        
    }


    //subir imagen de perfil
    public String subirImgPerfil(MultipartFile img){
        try {
            byte[] bytes = img.getBytes();
            String imgName = UUID.randomUUID().toString();

            
            String imgOriginalName = img.getOriginalFilename();
            if (imgOriginalName == null) {
                return "";
            }

            String imgExtencion = imgOriginalName.substring(imgOriginalName.lastIndexOf("."));

            String newImgName = imgName + imgExtencion;

            File folder = new File("user-pictures");
            if (!folder.exists()) {
                folder.mkdir();
            }

            Path path = Paths.get("user-pictures/" + newImgName); 

            Files.write(path, bytes);// crea el archibo

            return newImgName;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
        
    }

    //borra imagen perfil
    //borrar imagen receta
    public void deleteImgPerfil(String oldName){
        // Definir la ruta del archivo que se desea eliminar
        if (oldName != null && !oldName.trim().isEmpty()) {
            
            Path path = Paths.get("user-pictures/" + oldName);
            try {
                // Verificar si el archivo existe
                if (Files.exists(path)) {
                    // Eliminar el archivo
                    Files.delete(path);
                }
            } catch (Exception e) {
                System.out.println("Error al eliminar la imagen: " + e.getMessage());
            }
        }
        
    }

}
