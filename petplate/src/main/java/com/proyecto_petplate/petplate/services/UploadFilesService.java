package com.proyecto_petplate.petplate.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
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

    //private static String anuncioIzquierdoExtencion;
    //private static String anuncioDerechoExtencion;

    //subir anuncio
    public boolean subirAnuncio(String location,MultipartFile img){
        try {
            byte[] bytes = img.getBytes();


            File folder = new File("ads-pictures");
            if (!folder.exists()) {
                folder.mkdir();
            }

            String imgOriginalName = img.getOriginalFilename();
            if (imgOriginalName == null) {
                return false;
            }
            String imgExtencion = imgOriginalName.substring(imgOriginalName.lastIndexOf("."));

            if (location.equals("left")) {
                //agrega anuncio izquierdo
                Path path = Paths.get("ads-pictures/" + "anuncioIzquierdo" + imgExtencion); 
                Files.write(path, bytes);// crea el archivo
            }else if (location.equals("right")) {
                //agrega anuncio derecho
                Path path = Paths.get("ads-pictures/" + "anuncioDerecho" + imgExtencion); 
                Files.write(path, bytes);// crea el archivo
            }else if (location.equals("both")) {
                //agrega anuncio izquierdo
                Path path = Paths.get("ads-pictures/" + "anuncioIzquierdo" + imgExtencion); 
                Files.write(path, bytes);// crea el archivo
                //agrega anuncio derecho
                path = Paths.get("ads-pictures/" + "anuncioDerecho" + imgExtencion); 
                Files.write(path, bytes);// crea el archivo
                
                
            }else{
                return false;
            }

            return true;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    //borrar anuncio
    /* 
    public void deleteAnuncio(String location){
        // Definir la ruta del archivo que se desea eliminar
        if (location != null && !location.trim().isEmpty()) {
            
            
            try {
    

                if (location.equals("left")) {
                    //elimina el anuncio izquierdo
                    Path path = Paths.get("ads-pictures/" + "anuncioIzquierdo" + anuncioIzquierdoExtencion); 
                    // Verificar si el archivo existe
                    if (Files.exists(path)) {
                        // Eliminar el archivo
                        Files.delete(path);
                    }
                }else if (location.equals("right")) {
                    //elimina el anuncio derecho
                    Path path = Paths.get("ads-pictures/" + "anuncioDerecho" + anuncioDerechoExtencion); 
                    // Verificar si el archivo existe
                    if (Files.exists(path)) {
                        // Eliminar el archivo
                        Files.delete(path);
                    }
                }else if (location.equals("both")) {
                    //elimina el anuncio izquierdo
                    Path path = Paths.get("ads-pictures/" + "anuncioIzquierdo"+ anuncioIzquierdoExtencion) ; 
                    // Verificar si el archivo existe
                    if (Files.exists(path)) {
                        // Eliminar el archivo
                        Files.delete(path);
                    }else{
                        System.out.println("ads-pictures/" + "anuncioIzquierdo"+ anuncioIzquierdoExtencion);
                        System.out.println(anuncioDerechoExtencion);
                        System.out.println(anuncioIzquierdoExtencion);
                    }
                    //elimina el anuncio derecho
                    path = Paths.get("ads-pictures/" + "anuncioDerecho" + anuncioDerechoExtencion); 
                    // Verificar si el archivo existe
                    if (Files.exists(path)) {
                        // Eliminar el archivo
                        Files.delete(path);
                    }
                    
                }
                
            } catch (Exception e) {
                System.out.println("Error al eliminar la imagen: " + e.getMessage());
            }
        }
    }
    */

    public void deleteAnuncio(String location) {
        if (location != null && !location.trim().isEmpty()) {
            try {
                Path folderPath = Paths.get("ads-pictures");
                
                if (location.equals("left")) {
                    Files.list(folderPath)
                        .filter(path -> path.getFileName().toString().startsWith("anuncioIzquierdo"))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.out.println("Error al eliminar el archivo: " + path + " - " + e.getMessage());
                            }
                        });
                } else if (location.equals("right")) {
                    Files.list(folderPath)
                        .filter(path -> path.getFileName().toString().startsWith("anuncioDerecho"))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.out.println("Error al eliminar el archivo: " + path + " - " + e.getMessage());
                            }
                        });
                } else if (location.equals("both")) {
                    Files.list(folderPath)
                        .filter(path -> path.getFileName().toString().startsWith("anuncioIzquierdo") || path.getFileName().toString().startsWith("anuncioDerecho"))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.out.println("Error al eliminar el archivo: " + path + " - " + e.getMessage());
                            }
                        });
                }
            } catch (IOException e) {
                System.out.println("Error al listar o eliminar archivos: " + e.getMessage());
            }
        }
    }
}

