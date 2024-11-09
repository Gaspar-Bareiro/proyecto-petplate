package com.proyecto_petplate.petplate.Configuration;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class configuracionExterna {
    
    public static String  baseUrl = cargarUrlDesdeArchivo("configuration.txt");
    
    private static String cargarUrlDesdeArchivo(String rutaArchivo) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            // Agregar cada línea al conjunto, ignorando la primera (encabezado)
            String valorUrl = null;
            boolean encontrado = false;
            for (int i = 0; i < lineas.size(); i++) {
                valorUrl = lineas.get(i).trim();
                System.out.println(valorUrl);
                if (valorUrl.startsWith("URL=")) {
                    valorUrl = valorUrl.substring(4).trim();
                    encontrado = true;
                    break;
                }
    

            }
            if (encontrado) {
                return valorUrl; 
            }else{
                System.out.println("no se encontro la Url");
                return "";
            }
            
        } catch (IOException e) {
            
            System.err.println("Error al leer el archivo de unidades de medida: " + e.getMessage());
            return "";
        }
    }
    
}
