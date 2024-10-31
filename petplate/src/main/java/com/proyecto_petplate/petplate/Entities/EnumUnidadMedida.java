package com.proyecto_petplate.petplate.Entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EnumUnidadMedida {
    // Contenedor para unidades de medida válidas cargadas desde el archivo
    private static final Set<String> unidadesMedida = new LinkedHashSet<>();

    // Cargar unidades de medida desde un archivo
    static {
        cargarUnidadesDesdeArchivo("unidades_medida.txt");
    }

    // Método para cargar las unidades de medida desde el archivo
    private static void cargarUnidadesDesdeArchivo(String rutaArchivo) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            // Agregar cada línea al conjunto, ignorando la primera (encabezado)
            for (int i = 1; i < lineas.size(); i++) {
                unidadesMedida.add(lineas.get(i).trim());
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de unidades de medida: " + e.getMessage());
        }
    }

    // Método para verificar si una unidad de medida es válida
    public static boolean isValidUnit(String unidad) {
        return unidadesMedida.contains(unidad);
    }

    public static Set<String> getUnidadesMedida() {
        return new LinkedHashSet<>(unidadesMedida); // Retorna las unidades de medida en su formato original
    }
}
