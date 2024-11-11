package com.proyecto_petplate.petplate.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:./application.properties")
public class CustomPropertyConfig {
    // Esta clase permite que Spring cargue el archivo de propiedades desde una ubicación externa.
}
