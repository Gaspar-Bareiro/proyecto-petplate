package com.proyecto_petplate.petplate.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Ruta para las imágenes
        registry.addResourceHandler("/recipe-pictures/**")  // Acceso a /recipe-pictures/*
               .addResourceLocations("file:./recipe-pictures/");  // Ruta relativa al directorio donde se ejecuta el JAR

        registry.addResourceHandler("/user-pictures/**")  // Acceso a /recipe-pictures/*
               .addResourceLocations("file:./user-pictures/");  // Ruta relativa al directorio donde se ejecuta el JAR
    }
}